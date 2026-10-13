package com.example.engine

import com.example.model.BlockPiece
import com.example.model.BoardCell
import com.example.model.ComboFeedback
import com.example.model.GemType

object BoardLogic {

    fun createInitialBoard(
        rows: Int = 8,
        cols: Int = 8,
        obstacles: List<Pair<Int, Int>> = emptyList(),
        gems: List<Triple<Int, Int, GemType>> = emptyList()
    ): List<List<BoardCell>> {
        val obstacleSet = obstacles.toSet()
        val gemMap = gems.associate { Pair(it.first, it.second) to it.third }

        return List(rows) { r ->
            List(cols) { c ->
                val coord = Pair(r, c)
                when {
                    obstacleSet.contains(coord) -> {
                        BoardCell(occupied = true, gemType = GemType.OBSTACLE, isObstacle = true)
                    }
                    gemMap.containsKey(coord) -> {
                        val gem = gemMap[coord]!!
                        BoardCell(occupied = true, gemType = gem, isTargetGoal = gem == GemType.FROZEN)
                    }
                    else -> BoardCell()
                }
            }
        }
    }

    fun canPlace(
        board: List<List<BoardCell>>,
        piece: BlockPiece,
        targetRow: Int,
        targetCol: Int
    ): Boolean {
        val rows = board.size
        val cols = board.firstOrNull()?.size ?: return false

        for (r in 0 until piece.rows) {
            for (c in 0 until piece.cols) {
                if (piece.matrix[r][c]) {
                    val boardR = targetRow + r
                    val boardC = targetCol + c
                    if (boardR !in 0 until rows || boardC !in 0 until cols) {
                        return false
                    }
                    if (board[boardR][boardC].occupied) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun placePiece(
        board: List<List<BoardCell>>,
        piece: BlockPiece,
        targetRow: Int,
        targetCol: Int
    ): List<List<BoardCell>> {
        val mutable = board.map { it.toMutableList() }.toMutableList()
        for (r in 0 until piece.rows) {
            for (c in 0 until piece.cols) {
                if (piece.matrix[r][c]) {
                    val boardR = targetRow + r
                    val boardC = targetCol + c
                    mutable[boardR][boardC] = BoardCell(
                        occupied = true,
                        gemType = piece.gemType
                    )
                }
            }
        }
        return mutable.map { it.toList() }
    }

    fun detectFullLines(board: List<List<BoardCell>>): Pair<List<Int>, List<Int>> {
        val rows = board.size
        val cols = board.firstOrNull()?.size ?: return Pair(emptyList(), emptyList())

        val fullRows = mutableListOf<Int>()
        for (r in 0 until rows) {
            if (board[r].all { it.occupied }) {
                fullRows.add(r)
            }
        }

        val fullCols = mutableListOf<Int>()
        for (c in 0 until cols) {
            var full = true
            for (r in 0 until rows) {
                if (!board[r][c].occupied) {
                    full = false
                    break
                }
            }
            if (full) {
                fullCols.add(c)
            }
        }

        return Pair(fullRows, fullCols)
    }

    fun markLinesClearing(
        board: List<List<BoardCell>>,
        fullRows: List<Int>,
        fullCols: List<Int>
    ): List<List<BoardCell>> {
        val rowSet = fullRows.toSet()
        val colSet = fullCols.toSet()
        val rows = board.size
        val cols = board.firstOrNull()?.size ?: return board

        return List(rows) { r ->
            List(cols) { c ->
                val cell = board[r][c]
                if (cell.occupied && (rowSet.contains(r) || colSet.contains(c))) {
                    cell.copy(isClearing = true)
                } else {
                    cell
                }
            }
        }
    }

    fun clearLines(
        board: List<List<BoardCell>>,
        fullRows: List<Int>,
        fullCols: List<Int>
    ): List<List<BoardCell>> {
        val rowSet = fullRows.toSet()
        val colSet = fullCols.toSet()
        val rows = board.size
        val cols = board.firstOrNull()?.size ?: return board

        return List(rows) { r ->
            List(cols) { c ->
                if (rowSet.contains(r) || colSet.contains(c)) {
                    BoardCell()
                } else {
                    board[r][c]
                }
            }
        }
    }

    fun calculatePlacementScore(piece: BlockPiece): Int {
        // En Block Blast no se otorgan puntos por solo colocar piezas.
        // Los puntos se obtienen exclusivamente al completar líneas y combos.
        return 0
    }

    fun calculateLineClearFeedback(
        linesCleared: Int,
        currentStreak: Int
    ): ComboFeedback {
        val basePoints = when (linesCleared) {
            1 -> 100
            2 -> 300 // double line bonus
            3 -> 650 // triple line bonus
            4 -> 1200 // quad blast
            else -> 1200 + (linesCleared - 4) * 500
        }

        val multiplier = if (currentStreak > 0) currentStreak + 1 else 1
        val totalPoints = basePoints * multiplier

        val title = when {
            multiplier >= 5 -> "¡INCREÍBLE x$multiplier!"
            multiplier == 4 -> "¡MEGA COMBO x4!"
            multiplier == 3 -> "¡SUPER COMBO x3!"
            multiplier == 2 -> "¡COMBO x2!"
            linesCleared >= 3 -> "¡TRIPLE BLAST!"
            linesCleared == 2 -> "¡DOBLE BLAST!"
            else -> "¡LÍNEA LIMPIA!"
        }

        return ComboFeedback(
            linesCleared = linesCleared,
            streakCount = currentStreak,
            multiplier = multiplier,
            bonusPoints = totalPoints,
            title = title
        )
    }

    fun canAnyPieceBePlaced(
        board: List<List<BoardCell>>,
        pieces: List<BlockPiece>
    ): Boolean {
        if (pieces.isEmpty()) return true
        val rows = board.size
        val cols = board.firstOrNull()?.size ?: return false

        for (piece in pieces) {
            for (r in 0 until rows) {
                for (c in 0 until cols) {
                    if (canPlace(board, piece, r, c)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
