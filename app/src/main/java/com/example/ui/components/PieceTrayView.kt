package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BlockPiece
import com.example.ui.theme.*

/**
 * PieceTrayView renders the 3 available polyomino pieces in the waiting area.
 * Measures the exact offset between the touch point and the block's center to ensure
 * smooth, jump-free dragging and snapping.
 */
@Composable
fun PieceTrayView(
    trayPieces: List<BlockPiece?>,
    selectedTrayIndex: Int?,
    activeDragIndex: Int?,
    onPieceSelect: (index: Int) -> Unit,
    onDragStart: (index: Int, startPositionInRoot: Offset, touchToCenterOffset: Offset) -> Unit,
    onDragMove: (index: Int, dragPositionInRoot: Offset) -> Unit,
    onDragRelease: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("piece_tray_view")
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .shadow(10.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WoodDarkSurface, WoodDarkBackground)
                )
            )
            .border(2.dp, WoodBoardBorder, RoundedCornerShape(18.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hint text
            Text(
                text = if (selectedTrayIndex != null) "Toca una celda en el tablero para colocar" else "Arrastra o toca una pieza para jugar",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selectedTrayIndex != null) WoodGoldAccent else WoodTextSecondary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                trayPieces.forEachIndexed { index, piece ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (piece != null) {
                            TraySlotItem(
                                piece = piece,
                                index = index,
                                isSelected = selectedTrayIndex == index,
                                isDragging = activeDragIndex == index,
                                onSelect = { onPieceSelect(index) },
                                onDragStart = { pos, offsetToCenter -> onDragStart(index, pos, offsetToCenter) },
                                onDragMove = { pos -> onDragMove(index, pos) },
                                onDragRelease = { onDragRelease(index) }
                            )
                        } else {
                            // Empty slot placeholder
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(WoodCellEmpty.copy(alpha = 0.5f))
                                    .border(1.dp, WoodCellEmptyBorder.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TraySlotItem(
    piece: BlockPiece,
    index: Int,
    isSelected: Boolean,
    isDragging: Boolean,
    onSelect: () -> Unit,
    onDragStart: (touchInRoot: Offset, touchToCenterOffset: Offset) -> Unit,
    onDragMove: (Offset) -> Unit,
    onDragRelease: () -> Unit
) {
    var itemOriginInRoot by remember { mutableStateOf(Offset.Zero) }
    var itemSize by remember { mutableStateOf(Size.Zero) }
    var currentTouchInRoot by remember { mutableStateOf(Offset.Zero) }

    val pulseTransition = rememberInfiniteTransition(label = "pulse_selected")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .scale(if (isSelected) pulseScale else 1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) WoodPlaque else Color.Transparent
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) WoodGoldAccent else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .onGloballyPositioned { coordinates ->
                itemOriginInRoot = coordinates.positionInRoot()
                itemSize = Size(coordinates.size.width.toFloat(), coordinates.size.height.toFloat())
            }
            .clickable { onSelect() }
            .pointerInput(piece.id) {
                detectDragGestures(
                    onDragStart = { localOffset ->
                        currentTouchInRoot = itemOriginInRoot + localOffset
                        // Measure exact center of the block slot
                        val blockCenterInRoot = itemOriginInRoot + Offset(itemSize.width / 2f, itemSize.height / 2f)
                        val touchToCenterOffset = currentTouchInRoot - blockCenterInRoot
                        onDragStart(currentTouchInRoot, touchToCenterOffset)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        currentTouchInRoot += dragAmount
                        onDragMove(currentTouchInRoot)
                    },
                    onDragEnd = {
                        onDragRelease()
                    },
                    onDragCancel = {
                        onDragRelease()
                    }
                )
            }
            .alpha(if (isDragging) 0.35f else 1f),
        contentAlignment = Alignment.Center
    ) {
        PieceGrid(
            piece = piece,
            blockSize = 22.dp,
            modifier = Modifier.testTag("piece_tray_item_$index")
        )
    }
}

@Composable
fun PieceGrid(
    piece: BlockPiece,
    blockSize: Dp = 22.dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (r in 0 until piece.rows) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (c in 0 until piece.cols) {
                    if (piece.matrix[r][c]) {
                        GemBlock(
                            gemType = piece.gemType,
                            size = blockSize
                        )
                    } else {
                        Spacer(modifier = Modifier.size(blockSize))
                    }
                }
            }
        }
    }
}
