package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.engine.BoardLogic
import com.example.model.GameMode
import com.example.model.Screen
import com.example.ui.BlockBlastViewModel
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun GamePlayScreen(
    viewModel: BlockBlastViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val density = LocalDensity.current

    var boardOffset by remember { mutableStateOf(Offset.Zero) }
    var boardSize by remember { mutableStateOf(Size.Zero) }
    var boardRows by remember { mutableIntStateOf(8) }
    var boardCols by remember { mutableIntStateOf(8) }

    // Track touch position and the exact offset between the touch point and the block's center
    var dragTouchPositionInRoot by remember { mutableStateOf<Offset?>(null) }
    var dragTouchToCenterOffset by remember { mutableStateOf(Offset.Zero) }

    // Helper to calculate target board coordinates based on the block's true center
    fun calculateTargetCell(blockCenterX: Float, blockCenterY: Float, pieceRows: Int, pieceCols: Int): Pair<Int?, Int?> {
        if (boardSize.width <= 0 || boardSize.height <= 0 || boardRows <= 0 || boardCols <= 0) {
            return Pair(null, null)
        }

        val cellWidth = boardSize.width / boardCols
        val cellHeight = boardSize.height / boardRows

        val pieceWidthPx = pieceCols * cellWidth
        val pieceHeightPx = pieceRows * cellHeight

        // Top-left origin of the piece relative to board
        val pieceLeftBoardX = (blockCenterX - pieceWidthPx / 2f) - boardOffset.x
        val pieceTopBoardY = (blockCenterY - pieceHeightPx / 2f) - boardOffset.y

        val col = (pieceLeftBoardX / cellWidth).roundToInt()
        val row = (pieceTopBoardY / cellHeight).roundToInt()

        return if (row in 0 until boardRows && col in 0 until boardCols) {
            Pair(row, col)
        } else {
            Pair(null, null)
        }
    }

    // Intercept hardware back button to show pause dialog
    BackHandler {
        if (!state.isGameOver && !state.isLevelComplete && !state.isPaused) {
            viewModel.pauseGame()
        } else if (state.isPaused) {
            viewModel.resumeGame()
        } else {
            viewModel.navigateTo(Screen.HOME)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WoodDarkBackground,
                        WoodDarkSurface,
                        WoodBoardInner,
                        WoodDarkBackground
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top HUD
            TopGameHud(
                score = state.score,
                highScore = state.highScore,
                gameMode = state.gameMode,
                levelConfig = state.levelConfig,
                movesLeft = state.movesLeft,
                comboStreak = state.comboStreak,
                soundEnabled = state.soundEnabled,
                onPauseClick = { viewModel.pauseGame() },
                onSoundToggle = { viewModel.toggleSound() }
            )

            // Center: Wooden Game Board
            val activePiece = (state.draggedPieceIndex ?: state.selectedTrayIndex)?.let {
                state.trayPieces.getOrNull(it)
            }

            WoodenBoardView(
                board = state.board,
                draggedPiece = activePiece,
                previewRow = state.previewRow,
                previewCol = state.previewCol,
                onCellClick = { r, c ->
                    viewModel.onBoardCellClick(r, c)
                },
                onBoardBoundsChanged = { offset, size, rows, cols ->
                    boardOffset = offset
                    boardSize = size
                    boardRows = rows
                    boardCols = cols
                },
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(vertical = 4.dp)
            )

            // Bottom: Piece Tray View
            PieceTrayView(
                trayPieces = state.trayPieces,
                selectedTrayIndex = state.selectedTrayIndex,
                activeDragIndex = state.draggedPieceIndex,
                onPieceSelect = { idx ->
                    viewModel.selectPiece(idx)
                },
                onDragStart = { idx, touchPos, touchToCenterOffset ->
                    dragTouchPositionInRoot = touchPos
                    dragTouchToCenterOffset = touchToCenterOffset

                    val piece = state.trayPieces.getOrNull(idx)
                    if (piece != null) {
                        val blockCenterX = touchPos.x
                        val blockCenterY = touchPos.y
                        val (r, c) = calculateTargetCell(blockCenterX, blockCenterY, piece.rows, piece.cols)
                        viewModel.setDragging(idx, r, c)
                    } else {
                        viewModel.setDragging(idx, null, null)
                    }
                },
                onDragMove = { idx, touchPos ->
                    dragTouchPositionInRoot = touchPos
                    val piece = state.trayPieces.getOrNull(idx)
                    if (piece != null) {
                        val blockCenterX = touchPos.x
                        val blockCenterY = touchPos.y
                        val (r, c) = calculateTargetCell(blockCenterX, blockCenterY, piece.rows, piece.cols)
                        viewModel.setDragging(idx, r, c)
                    }
                },
                onDragRelease = { idx ->
                    val r = state.previewRow
                    val c = state.previewCol
                    dragTouchPositionInRoot = null
                    dragTouchToCenterOffset = Offset.Zero
                    if (r != null && c != null) {
                        viewModel.attemptPlacePiece(idx, r, c)
                    } else {
                        viewModel.setDragging(null, null, null)
                    }
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Global Unclipped Floating Drag Overlay with Precision Offset & Smooth Snap
        if (state.draggedPieceIndex != null && dragTouchPositionInRoot != null) {
            val draggedPiece = state.trayPieces.getOrNull(state.draggedPieceIndex!!)
            if (draggedPiece != null) {
                val cellWidth = if (boardCols > 0 && boardSize.width > 0) boardSize.width / boardCols else with(density) { 36.dp.toPx() }
                val cellHeight = if (boardRows > 0 && boardSize.height > 0) boardSize.height / boardRows else with(density) { 36.dp.toPx() }

                val pieceWidthPx = draggedPiece.cols * cellWidth
                val pieceHeightPx = draggedPiece.rows * cellHeight

                // The block's true center aligned with touch position
                val blockCenterX = dragTouchPositionInRoot!!.x
                val blockCenterY = dragTouchPositionInRoot!!.y

                val freeLeftX = blockCenterX - (pieceWidthPx / 2f)
                val freeTopY = blockCenterY - (pieceHeightPx / 2f)

                // Check snapping to valid grid position without jumping
                val pRow = state.previewRow
                val pCol = state.previewCol
                val hasValidSnap = pRow != null && pCol != null && BoardLogic.canPlace(state.board, draggedPiece, pRow, pCol)

                val (renderX, renderY) = if (hasValidSnap) {
                    val gridSnappedX = boardOffset.x + pCol!! * cellWidth
                    val gridSnappedY = boardOffset.y + pRow!! * cellHeight

                    val distX = abs(freeLeftX - gridSnappedX)
                    val distY = abs(freeTopY - gridSnappedY)
                    val snapDistance = cellWidth * 0.9f

                    // Soft magnetic guide: smoothly pulls toward grid cell when hovering near it
                    if (distX < snapDistance && distY < snapDistance) {
                        val factor = 0.5f // Smooth magnetic lerp
                        Pair(
                            freeLeftX + (gridSnappedX - freeLeftX) * factor,
                            freeTopY + (gridSnappedY - freeTopY) * factor
                        )
                    } else {
                        Pair(freeLeftX, freeTopY)
                    }
                } else {
                    Pair(freeLeftX, freeTopY)
                }

                val boardCellSize = with(density) { (cellWidth - 1.dp.toPx()).toDp() }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(99f)
                ) {
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(renderX.roundToInt(), renderY.roundToInt())
                        }
                    ) {
                        PieceGrid(
                            piece = draggedPiece,
                            blockSize = boardCellSize
                        )
                    }
                }
            }
        }

        // Particle Burst Overlay (Canvas)
        ParticleOverlay(
            particles = state.particles,
            modifier = Modifier.fillMaxSize()
        )

        // Floating Combo Banner
        ComboBannerOverlay(
            feedback = state.lastComboFeedback,
            onDismiss = { viewModel.clearComboBanner() },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 60.dp)
        )

        // Dialogs
        if (state.isPaused) {
            PauseDialog(
                onResume = { viewModel.resumeGame() },
                onRestart = { viewModel.restartCurrentGame() },
                onHome = { viewModel.navigateTo(Screen.HOME) }
            )
        }

        if (state.isGameOver) {
            GameOverDialog(
                score = state.score,
                highScore = state.highScore,
                isNewRecord = state.score >= state.highScore && state.score > 0,
                onRestart = { viewModel.restartCurrentGame() },
                onHome = { viewModel.navigateTo(Screen.HOME) }
            )
        }

        if (state.isLevelComplete) {
            val currentLevelId = (state.gameMode as? GameMode.Adventure)?.levelId ?: 1
            LevelCompleteDialog(
                score = state.score,
                starsEarned = state.starsEarned,
                nextLevelId = currentLevelId + 1,
                onNextLevel = {
                    viewModel.startAdventureLevel(currentLevelId + 1)
                },
                onLevelMap = {
                    viewModel.navigateTo(Screen.LEVEL_SELECT)
                }
            )
        }
    }
}
