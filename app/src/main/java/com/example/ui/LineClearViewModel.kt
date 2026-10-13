package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.engine.BoardLogic
import com.example.model.BoardCell
import com.example.model.ComboFeedback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * State representing line clears, scoring, and combo streak multipliers.
 */
data class LineClearState(
    val score: Int = 0,
    val comboStreak: Int = 0,
    val currentMultiplier: Int = 1,
    val lastLinesCleared: Int = 0,
    val lastPointsEarned: Int = 0,
    val lastComboFeedback: ComboFeedback? = null,
    val fullRows: List<Int> = emptyList(),
    val fullCols: List<Int> = emptyList()
)

/**
 * Detailed result of a line detection and scoring calculation.
 */
data class LineDetectionResult(
    val fullRows: List<Int>,
    val fullCols: List<Int>,
    val totalLines: Int,
    val pointsAwarded: Int,
    val newStreak: Int,
    val multiplier: Int,
    val feedback: ComboFeedback?
)

/**
 * LineClearViewModel manages:
 * 1. Detection of simultaneously completed rows and columns on the board.
 * 2. Point calculation with multi-line clear bonuses.
 * 3. Consecutive turn combo streak multipliers (x2, x3, x4, x5...).
 * 4. Reactive StateFlow exposure for Jetpack Compose UI.
 */
class LineClearViewModel : ViewModel() {

    private val _state = MutableStateFlow(LineClearState())
    val state: StateFlow<LineClearState> = _state.asStateFlow()

    /**
     * Detects all completely filled rows and columns in the board.
     */
    fun detectCompletedLines(board: List<List<BoardCell>>): Pair<List<Int>, List<Int>> {
        return BoardLogic.detectFullLines(board)
    }

    /**
     * Calculates base points for lines destroyed simultaneously in a single move.
     */
    fun calculateBasePoints(linesCleared: Int): Int {
        return when (linesCleared) {
            0 -> 0
            1 -> 100
            2 -> 300 // Double line clear bonus
            3 -> 650 // Triple line clear bonus
            4 -> 1200 // Quad blast bonus
            else -> 1200 + (linesCleared - 4) * 500
        }
    }

    /**
     * Calculates consecutive turn streak multiplier:
     * Streak 1 -> x2
     * Streak 2 -> x3
     * Streak 3 -> x4
     * Streak N -> x(N+1)
     */
    fun getMultiplierForStreak(streak: Int): Int {
        return if (streak > 0) streak + 1 else 1
    }

    /**
     * Evaluates the current board state:
     * - Detects completed lines.
     * - If lines > 0: increments streak, applies multiplier, awards points, and updates state.
     * - If lines == 0: resets consecutive combo streak to 0.
     */
    fun processBoardLines(board: List<List<BoardCell>>): LineDetectionResult {
        val (fullRows, fullCols) = detectCompletedLines(board)
        val linesCount = fullRows.size + fullCols.size

        val currentStreak = _state.value.comboStreak

        val result = if (linesCount > 0) {
            val newStreak = currentStreak + 1
            val multiplier = getMultiplierForStreak(newStreak)
            val basePoints = calculateBasePoints(linesCount)
            val totalPoints = basePoints * multiplier

            val feedback = BoardLogic.calculateLineClearFeedback(linesCount, newStreak)

            _state.update {
                it.copy(
                    score = it.score + totalPoints,
                    comboStreak = newStreak,
                    currentMultiplier = multiplier,
                    lastLinesCleared = linesCount,
                    lastPointsEarned = totalPoints,
                    lastComboFeedback = feedback,
                    fullRows = fullRows,
                    fullCols = fullCols
                )
            }

            LineDetectionResult(
                fullRows = fullRows,
                fullCols = fullCols,
                totalLines = linesCount,
                pointsAwarded = totalPoints,
                newStreak = newStreak,
                multiplier = multiplier,
                feedback = feedback
            )
        } else {
            // Reset consecutive combo streak when a move does not clear any line
            _state.update {
                it.copy(
                    comboStreak = 0,
                    currentMultiplier = 1,
                    lastLinesCleared = 0,
                    lastPointsEarned = 0,
                    lastComboFeedback = null,
                    fullRows = emptyList(),
                    fullCols = emptyList()
                )
            }

            LineDetectionResult(
                fullRows = emptyList(),
                fullCols = emptyList(),
                totalLines = 0,
                pointsAwarded = 0,
                newStreak = 0,
                multiplier = 1,
                feedback = null
            )
        }

        return result
    }

    /**
     * Resets score, combo streak, and active clearing data (e.g. on new game).
     */
    fun resetScoreAndStreak() {
        _state.value = LineClearState()
    }
}
