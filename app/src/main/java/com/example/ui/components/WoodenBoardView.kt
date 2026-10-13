package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.BlockPiece
import com.example.model.BoardCell
import com.example.ui.theme.*

/**
 * WoodenBoardView renders an interactive game board with:
 * - Dynamic dimension support (e.g. 7x7, 8x8, or custom NxM grids)
 * - Rich procedural wood grain textures on the outer carved frame and individual cells
 * - Real-time ghost placement preview for polyomino pieces
 * - Precise touch coordinate reporting for drag-and-drop gameplay
 */
@Composable
fun WoodenBoardView(
    board: List<List<BoardCell>>,
    draggedPiece: BlockPiece?,
    previewRow: Int?,
    previewCol: Int?,
    modifier: Modifier = Modifier,
    onCellClick: (row: Int, col: Int) -> Unit = { _, _ -> },
    onBoardBoundsChanged: (boardOffset: Offset, boardSize: Size, rows: Int, cols: Int) -> Unit = { _, _, _, _ -> }
) {
    val rows = board.size.coerceAtLeast(1)
    val cols = (board.firstOrNull()?.size ?: 8).coerceAtLeast(1)

    Box(
        modifier = modifier
            .testTag("wooden_game_board")
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .shadow(elevation = 14.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WoodBoardBorder,
                        WoodBoardInner,
                        WoodBoardBorder
                    )
                )
            )
            .border(
                width = 3.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        WoodGoldBorder,
                        WoodBoardBorder,
                        WoodGoldAccent.copy(alpha = 0.7f),
                        WoodBoardBorder
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(8.dp)
    ) {
        // Outer wooden frame grain canvas
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            // Decorative wood border grooves
            drawLine(
                color = WoodGoldAccent.copy(alpha = 0.35f),
                start = Offset(14f, 14f),
                end = Offset(w - 14f, 14f),
                strokeWidth = 1.2f
            )
            drawLine(
                color = WoodGoldAccent.copy(alpha = 0.35f),
                start = Offset(14f, h - 14f),
                end = Offset(w - 14f, h - 14f),
                strokeWidth = 1.2f
            )
        }

        // Corner gold rivets for rich handcrafted look
        GoldRivet(modifier = Modifier.align(Alignment.TopStart).padding(3.dp))
        GoldRivet(modifier = Modifier.align(Alignment.TopEnd).padding(3.dp))
        GoldRivet(modifier = Modifier.align(Alignment.BottomStart).padding(3.dp))
        GoldRivet(modifier = Modifier.align(Alignment.BottomEnd).padding(3.dp))

        // Inner dynamic grid container
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(cols.toFloat() / rows.toFloat())
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(WoodBoardInner)
                .border(2.dp, WoodCellEmptyBorder, RoundedCornerShape(12.dp))
                .onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInRoot()
                    val size = coordinates.size
                    onBoardBoundsChanged(
                        pos,
                        Size(size.width.toFloat(), size.height.toFloat()),
                        rows,
                        cols
                    )
                }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                for (r in 0 until rows) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (c in 0 until cols) {
                            val cell = board.getOrNull(r)?.getOrNull(c) ?: BoardCell()

                            // Check if current cell is within ghost piece preview
                            val isPreviewCell = if (draggedPiece != null && previewRow != null && previewCol != null) {
                                val pRow = r - previewRow
                                val pCol = c - previewCol
                                if (pRow in 0 until draggedPiece.rows && pCol in 0 until draggedPiece.cols) {
                                    draggedPiece.matrix[pRow][pCol]
                                } else false
                            } else false

                            WoodenCellView(
                                cell = cell,
                                row = r,
                                col = c,
                                isPreview = isPreviewCell,
                                previewGemType = draggedPiece?.gemType,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable { onCellClick(r, c) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoldRivet(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        WoodGoldAccent,
                        WoodGoldBorder,
                        WoodDarkBackground
                    )
                )
            )
            .border(0.8.dp, WoodGoldAccent.copy(alpha = 0.8f), CircleShape)
    )
}
