package com.example

import com.example.engine.BoardLogic
import com.example.model.BlockPiece
import com.example.model.GemType
import com.example.ui.LineClearViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testBoardCreation() {
    val board = BoardLogic.createInitialBoard(8, 8)
    assertEquals(8, board.size)
    assertEquals(8, board[0].size)
    assertTrue(board.all { row -> row.all { !it.occupied } })
  }

  @Test
  fun testPiecePlacement() {
    val board = BoardLogic.createInitialBoard(8, 8)
    val dot = BlockPiece(matrix = listOf(listOf(true)), gemType = GemType.RUBY)

    assertTrue(BoardLogic.canPlace(board, dot, 0, 0))

    val newBoard = BoardLogic.placePiece(board, dot, 0, 0)
    assertTrue(newBoard[0][0].occupied)
    assertEquals(GemType.RUBY, newBoard[0][0].gemType)
    assertFalse(BoardLogic.canPlace(newBoard, dot, 0, 0))
  }

  @Test
  fun testLineClearDetection() {
    var board = BoardLogic.createInitialBoard(8, 8)
    val dot = BlockPiece(matrix = listOf(listOf(true)), gemType = GemType.EMERALD)

    // Fill row 3
    for (c in 0 until 8) {
      board = BoardLogic.placePiece(board, dot, 3, c)
    }

    val (fullRows, fullCols) = BoardLogic.detectFullLines(board)
    assertEquals(listOf(3), fullRows)
    assertTrue(fullCols.isEmpty())

    val clearedBoard = BoardLogic.clearLines(board, fullRows, fullCols)
    assertTrue(clearedBoard[3].all { !it.occupied })
  }

  @Test
  fun testComboCalculation() {
    val singleLineFeedback = BoardLogic.calculateLineClearFeedback(1, 0)
    assertEquals(100, singleLineFeedback.bonusPoints)
    assertEquals(1, singleLineFeedback.multiplier)

    val streakFeedback = BoardLogic.calculateLineClearFeedback(2, 2)
    // base 300 * multiplier 3 = 900
    assertEquals(900, streakFeedback.bonusPoints)
    assertEquals(3, streakFeedback.multiplier)
    assertEquals("¡SUPER COMBO x3!", streakFeedback.title)
  }

  @Test
  fun testLineClearViewModelConsecutiveCombos() {
    val viewModel = LineClearViewModel()
    val dot = BlockPiece(matrix = listOf(listOf(true)), gemType = GemType.SAPPHIRE)

    // Turn 1: Complete row 0
    var board1 = BoardLogic.createInitialBoard(8, 8)
    for (c in 0 until 8) {
      board1 = BoardLogic.placePiece(board1, dot, 0, c)
    }
    val result1 = viewModel.processBoardLines(board1)
    assertEquals(1, result1.totalLines)
    assertEquals(1, result1.newStreak)
    assertEquals(2, result1.multiplier) // Streak 1 -> Multiplier x2
    assertEquals(200, result1.pointsAwarded) // 100 base * 2
    assertEquals(200, viewModel.state.value.score)

    // Turn 2: Complete row 1 (Consecutive clear)
    var board2 = BoardLogic.createInitialBoard(8, 8)
    for (c in 0 until 8) {
      board2 = BoardLogic.placePiece(board2, dot, 1, c)
    }
    val result2 = viewModel.processBoardLines(board2)
    assertEquals(1, result2.totalLines)
    assertEquals(2, result2.newStreak)
    assertEquals(3, result2.multiplier) // Streak 2 -> Multiplier x3
    assertEquals(300, result2.pointsAwarded) // 100 base * 3
    assertEquals(500, viewModel.state.value.score) // 200 + 300

    // Turn 3: No lines cleared
    val emptyBoard = BoardLogic.createInitialBoard(8, 8)
    val result3 = viewModel.processBoardLines(emptyBoard)
    assertEquals(0, result3.totalLines)
    assertEquals(0, result3.newStreak) // Reset streak
    assertEquals(1, result3.multiplier)
    assertEquals(0, result3.pointsAwarded)
    assertEquals(500, viewModel.state.value.score)

    // Turn 4: Double line clear (row 0 and col 0)
    var board4 = BoardLogic.createInitialBoard(8, 8)
    for (i in 0 until 8) {
      board4 = BoardLogic.placePiece(board4, dot, 0, i)
      board4 = BoardLogic.placePiece(board4, dot, i, 0)
    }
    val result4 = viewModel.processBoardLines(board4)
    assertEquals(2, result4.totalLines)
    assertEquals(1, result4.newStreak)
    assertEquals(2, result4.multiplier)
    assertEquals(600, result4.pointsAwarded) // 300 base * 2
    assertEquals(1100, viewModel.state.value.score)
  }

  @Test
  fun testNoPointsOnPiecePlacement() {
    val piece = BlockPiece(
      matrix = listOf(
        listOf(true, true),
        listOf(true, true)
      ),
      gemType = GemType.RUBY
    )
    val score = BoardLogic.calculatePlacementScore(piece)
    assertEquals(0, score)
  }
}
