package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.engine.AdventureLevels
import com.example.engine.BoardLogic
import com.example.engine.ShapeDefinitions
import com.example.model.*
import com.example.ui.theme.*
import com.example.util.SoundEffects
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class GameUiState(
    val currentScreen: Screen = Screen.HOME,
    val gameMode: GameMode = GameMode.Classic,
    val board: List<List<BoardCell>> = emptyList(),
    val trayPieces: List<BlockPiece?> = emptyList(),
    val score: Int = 0,
    val highScore: Int = 0,
    val comboStreak: Int = 0,
    val lastComboFeedback: ComboFeedback? = null,
    val movesLeft: Int? = null,
    val levelConfig: LevelConfig? = null,
    val isGameOver: Boolean = false,
    val isLevelComplete: Boolean = false,
    val starsEarned: Int = 0,
    val isPaused: Boolean = false,
    val selectedTrayIndex: Int? = null,
    val draggedPieceIndex: Int? = null,
    val previewRow: Int? = null,
    val previewCol: Int? = null,
    val isClearingAnimation: Boolean = false,
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true,
    val unlockedLevel: Int = 1,
    val totalStars: Int = 0,
    val particles: List<Particle> = emptyList()
)

class BlockBlastViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepository(application)
    private val soundEffects = SoundEffects(application)
    val lineClearViewModel = LineClearViewModel()

    private val _uiState = MutableStateFlow(
        GameUiState(
            highScore = repository.getClassicHighScore(),
            soundEnabled = repository.isSoundEnabled(),
            hapticEnabled = repository.isHapticEnabled(),
            unlockedLevel = repository.getUnlockedLevel(),
            totalStars = repository.getTotalStarsEarned()
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        // Start in home screen
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun startClassicGame() {
        val board = BoardLogic.createInitialBoard(rows = 8, cols = 8)
        val pieces = ShapeDefinitions.generateThreePieces()
        val highScore = repository.getClassicHighScore()
        lineClearViewModel.resetScoreAndStreak()

        _uiState.update {
            it.copy(
                currentScreen = Screen.GAMEPLAY,
                gameMode = GameMode.Classic,
                board = board,
                trayPieces = pieces,
                score = 0,
                highScore = highScore,
                comboStreak = 0,
                lastComboFeedback = null,
                movesLeft = null,
                levelConfig = null,
                isGameOver = false,
                isLevelComplete = false,
                starsEarned = 0,
                isPaused = false,
                draggedPieceIndex = null,
                previewRow = null,
                previewCol = null,
                particles = emptyList()
            )
        }
    }

    fun startAdventureLevel(levelId: Int) {
        val level = AdventureLevels.getLevel(levelId)
        val board = BoardLogic.createInitialBoard(
            rows = level.rows,
            cols = level.cols,
            obstacles = level.initialObstacles,
            gems = level.initialGems
        )
        val pieces = ShapeDefinitions.generateThreePieces()
        val levelHighScore = repository.getLevelHighScore(levelId)
        lineClearViewModel.resetScoreAndStreak()

        _uiState.update {
            it.copy(
                currentScreen = Screen.GAMEPLAY,
                gameMode = GameMode.Adventure(levelId),
                board = board,
                trayPieces = pieces,
                score = 0,
                highScore = levelHighScore,
                comboStreak = 0,
                lastComboFeedback = null,
                movesLeft = level.maxMoves,
                levelConfig = level,
                isGameOver = false,
                isLevelComplete = false,
                starsEarned = 0,
                isPaused = false,
                draggedPieceIndex = null,
                previewRow = null,
                previewCol = null,
                particles = emptyList()
            )
        }
    }

    fun restartCurrentGame() {
        val state = _uiState.value
        when (val mode = state.gameMode) {
            is GameMode.Classic -> startClassicGame()
            is GameMode.Adventure -> startAdventureLevel(mode.levelId)
        }
    }

    fun pauseGame() {
        _uiState.update { it.copy(isPaused = true) }
    }

    fun resumeGame() {
        _uiState.update { it.copy(isPaused = false) }
    }

    fun setDragging(index: Int?, row: Int? = null, col: Int? = null) {
        val state = _uiState.value
        if (state.isClearingAnimation || state.isGameOver || state.isLevelComplete) return

        if (index != null && state.draggedPieceIndex != index) {
            soundEffects.playPickUp(state.soundEnabled)
        }

        if (index == null || row == null || col == null) {
            _uiState.update {
                it.copy(
                    draggedPieceIndex = index,
                    previewRow = null,
                    previewCol = null
                )
            }
            return
        }

        val piece = state.trayPieces.getOrNull(index)
        val isValid = if (piece != null) {
            BoardLogic.canPlace(state.board, piece, row, col)
        } else false

        _uiState.update {
            it.copy(
                draggedPieceIndex = index,
                previewRow = if (isValid) row else null,
                previewCol = if (isValid) col else null
            )
        }
    }

    fun selectPiece(index: Int?) {
        val state = _uiState.value
        if (state.isClearingAnimation || state.isGameOver || state.isLevelComplete) return
        val currentSelection = state.selectedTrayIndex
        val newSelection = if (currentSelection == index) null else index
        if (newSelection != null) {
            soundEffects.playPickUp(state.soundEnabled)
        }
        _uiState.update {
            it.copy(
                selectedTrayIndex = newSelection,
                previewRow = null,
                previewCol = null
            )
        }
    }

    fun onBoardCellClick(row: Int, col: Int) {
        val state = _uiState.value
        val selectedIdx = state.selectedTrayIndex ?: return
        attemptPlacePiece(selectedIdx, row, col)
    }

    fun attemptPlacePiece(index: Int, targetRow: Int, targetCol: Int) {
        val state = _uiState.value
        if (state.isClearingAnimation || state.isGameOver || state.isLevelComplete) return

        val piece = state.trayPieces.getOrNull(index) ?: return
        if (!BoardLogic.canPlace(state.board, piece, targetRow, targetCol)) {
            // Invalid placement
            _uiState.update {
                it.copy(
                    draggedPieceIndex = null,
                    selectedTrayIndex = null,
                    previewRow = null,
                    previewCol = null
                )
            }
            return
        }

        viewModelScope.launch {
            // 1. Place piece (Points are ONLY awarded when lines are cleared)
            val newBoard = BoardLogic.placePiece(state.board, piece, targetRow, targetCol)
            var currentScore = state.score

            val newTray = state.trayPieces.toMutableList()
            newTray[index] = null

            val remainingMoves = state.movesLeft?.let { (it - 1).coerceAtLeast(0) }

            soundEffects.playPlace(state.soundEnabled, state.hapticEnabled)

            _uiState.update {
                it.copy(
                    board = newBoard,
                    trayPieces = newTray,
                    score = currentScore,
                    movesLeft = remainingMoves,
                    draggedPieceIndex = null,
                    selectedTrayIndex = null,
                    previewRow = null,
                    previewCol = null
                )
            }

            // 2. Check line clears using LineClearViewModel
            val lineResult = lineClearViewModel.processBoardLines(newBoard)

            if (lineResult.totalLines > 0) {
                // Animate line clearing
                _uiState.update { it.copy(isClearingAnimation = true) }
                val markedBoard = BoardLogic.markLinesClearing(newBoard, lineResult.fullRows, lineResult.fullCols)
                _uiState.update { it.copy(board = markedBoard) }

                currentScore += lineResult.pointsAwarded

                // Trigger sounds and haptics
                if (lineResult.multiplier > 1) {
                    soundEffects.playCombo(lineResult.multiplier, state.soundEnabled, state.hapticEnabled)
                } else {
                    soundEffects.playLineClear(state.soundEnabled, state.hapticEnabled)
                }

                // Generate burst particles
                spawnParticles(lineResult.fullRows, lineResult.fullCols, newBoard.size, newBoard.firstOrNull()?.size ?: 8)

                delay(280)

                val cleanedBoard = BoardLogic.clearLines(newBoard, lineResult.fullRows, lineResult.fullCols)
                _uiState.update {
                    it.copy(
                        board = cleanedBoard,
                        score = currentScore,
                        comboStreak = lineResult.newStreak,
                        lastComboFeedback = lineResult.feedback,
                        isClearingAnimation = false
                    )
                }
            } else {
                // Reset combo streak
                _uiState.update {
                    it.copy(
                        comboStreak = 0,
                        lastComboFeedback = null
                    )
                }
            }

            // Update high score in Classic mode
            if (state.gameMode is GameMode.Classic) {
                if (currentScore > state.highScore) {
                    repository.saveClassicHighScore(currentScore)
                    _uiState.update { it.copy(highScore = currentScore) }
                }
            }

            // Check if tray is empty -> replenish 3 new pieces
            var activeTray = _uiState.value.trayPieces
            if (activeTray.all { it == null }) {
                activeTray = ShapeDefinitions.generateThreePieces()
                _uiState.update { it.copy(trayPieces = activeTray) }
            }

            val availablePieces = activeTray.filterNotNull()

            // Check Adventure level completion
            val currentLevel = state.levelConfig
            if (currentLevel != null) {
                if (currentScore >= currentLevel.targetScore) {
                    // Brief delay so the user clearly sees the progress bar animate and fill to 100%
                    delay(500)

                    val stars = when {
                        currentScore >= currentLevel.star3Score -> 3
                        currentScore >= currentLevel.star2Score -> 2
                        else -> 1
                    }
                    repository.saveLevelScore(currentLevel.levelId, currentScore)
                    repository.saveLevelStars(currentLevel.levelId, stars)
                    repository.unlockNextLevel(currentLevel.levelId)

                    soundEffects.playLevelWin(state.soundEnabled, state.hapticEnabled)

                    _uiState.update {
                        it.copy(
                            isLevelComplete = true,
                            starsEarned = stars,
                            unlockedLevel = repository.getUnlockedLevel(),
                            totalStars = repository.getTotalStarsEarned()
                        )
                    }
                    return@launch
                } else if (remainingMoves != null && remainingMoves <= 0) {
                    // Out of moves in adventure
                    delay(300)
                    soundEffects.playGameOver(state.soundEnabled, state.hapticEnabled)
                    _uiState.update { it.copy(isGameOver = true) }
                    return@launch
                }
            }

            // Check Game Over (no valid placement remaining on board)
            val currentBoard = _uiState.value.board
            val canMove = BoardLogic.canAnyPieceBePlaced(currentBoard, availablePieces)
            if (!canMove) {
                soundEffects.playGameOver(state.soundEnabled, state.hapticEnabled)
                _uiState.update { it.copy(isGameOver = true) }
            }
        }
    }

    private fun spawnParticles(fullRows: List<Int>, fullCols: List<Int>, rows: Int, cols: Int) {
        val newParticles = mutableListOf<Particle>()
        val colors = listOf(RubyPrimary, SapphirePrimary, EmeraldPrimary, TopazPrimary, AmethystPrimary, DiamondPrimary, WoodGoldAccent)

        for (r in fullRows) {
            for (c in 0 until cols) {
                for (p in 0..3) {
                    val angle = Random.nextFloat() * 6.28f
                    val speed = Random.nextFloat() * 250f + 50f
                    newParticles.add(
                        Particle(
                            x = (c + 0.5f) / cols,
                            y = (r + 0.5f) / rows,
                            vx = kotlin.math.cos(angle) * speed,
                            vy = kotlin.math.sin(angle) * speed,
                            color = colors.random(),
                            size = Random.nextFloat() * 12f + 6f
                        )
                    )
                }
            }
        }

        for (c in fullCols) {
            for (r in 0 until rows) {
                for (p in 0..3) {
                    val angle = Random.nextFloat() * 6.28f
                    val speed = Random.nextFloat() * 250f + 50f
                    newParticles.add(
                        Particle(
                            x = (c + 0.5f) / cols,
                            y = (r + 0.5f) / rows,
                            vx = kotlin.math.cos(angle) * speed,
                            vy = kotlin.math.sin(angle) * speed,
                            color = colors.random(),
                            size = Random.nextFloat() * 12f + 6f
                        )
                    )
                }
            }
        }

        _uiState.update { it.copy(particles = newParticles.take(120)) }

        viewModelScope.launch {
            delay(650)
            _uiState.update { it.copy(particles = emptyList()) }
        }
    }

    fun toggleSound() {
        val newSound = !_uiState.value.soundEnabled
        repository.setSoundEnabled(newSound)
        _uiState.update { it.copy(soundEnabled = newSound) }
    }

    fun toggleHaptic() {
        val newHaptic = !_uiState.value.hapticEnabled
        repository.setHapticEnabled(newHaptic)
        _uiState.update { it.copy(hapticEnabled = newHaptic) }
    }

    fun clearComboBanner() {
        _uiState.update { it.copy(lastComboFeedback = null) }
    }

    override fun onCleared() {
        super.onCleared()
        soundEffects.release()
    }
}
