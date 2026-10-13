package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.model.BoardCell
import com.example.model.GemType
import com.example.ui.theme.*

@Composable
fun WoodenCellView(
    cell: BoardCell,
    row: Int,
    col: Int,
    isPreview: Boolean = false,
    previewGemType: GemType? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(1.5.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(WoodCellEmpty)
            .border(1.dp, WoodCellEmptyBorder, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Procedural Wood Texture Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            if (w <= 0f || h <= 0f) return@Canvas

            // 1. Inset 3D shadow for carved socket (inner bevel)
            // Top shadow
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.65f), Color.Transparent),
                    startY = 0f,
                    endY = h * 0.35f
                ),
                size = Size(w, h * 0.35f)
            )
            // Left shadow
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent),
                    startX = 0f,
                    endX = w * 0.35f
                ),
                size = Size(w * 0.35f, h)
            )
            // Bottom-Right subtle bevel reflection
            drawLine(
                color = WoodBevelHighlight.copy(alpha = 0.5f),
                start = Offset(0f, h - 1f),
                end = Offset(w, h - 1f),
                strokeWidth = 1.5f
            )
            drawLine(
                color = WoodBevelHighlight.copy(alpha = 0.5f),
                start = Offset(w - 1f, 0f),
                end = Offset(w - 1f, h),
                strokeWidth = 1.5f
            )

            // 2. Parquet Wood Grain: Alternates orientation based on coordinates
            val isHorizontalGrain = (row + col) % 2 == 0

            if (isHorizontalGrain) {
                // Horizontal curved wood fibers
                val fiberOffsets = listOf(0.22f, 0.45f, 0.68f, 0.85f)
                fiberOffsets.forEachIndexed { idx, yFrac ->
                    val y = h * yFrac
                    val path = Path().apply {
                        moveTo(w * 0.08f, y)
                        quadraticTo(
                            w * 0.5f,
                            y + (if (idx % 2 == 0) -h * 0.06f else h * 0.06f),
                            w * 0.92f,
                            y
                        )
                    }
                    drawPath(
                        path = path,
                        color = if (idx % 2 == 0) WoodGrainFiber.copy(alpha = 0.35f) else WoodCarveShadow.copy(alpha = 0.4f),
                        style = Stroke(width = 1.5f, cap = StrokeCap.Round)
                    )
                }
            } else {
                // Vertical curved wood fibers
                val fiberOffsets = listOf(0.22f, 0.45f, 0.68f, 0.85f)
                fiberOffsets.forEachIndexed { idx, xFrac ->
                    val x = w * xFrac
                    val path = Path().apply {
                        moveTo(x, h * 0.08f)
                        quadraticTo(
                            x + (if (idx % 2 == 0) -w * 0.06f else w * 0.06f),
                            h * 0.5f,
                            x,
                            h * 0.92f
                        )
                    }
                    drawPath(
                        path = path,
                        color = if (idx % 2 == 0) WoodGrainFiber.copy(alpha = 0.35f) else WoodCarveShadow.copy(alpha = 0.4f),
                        style = Stroke(width = 1.5f, cap = StrokeCap.Round)
                    )
                }
            }

            // 3. Subtle wood knot ring for organic realism on select cells
            if ((row * 7 + col * 13) % 5 == 0) {
                val knotCenterX = w * 0.55f
                val knotCenterY = h * 0.55f
                drawCircle(
                    color = WoodGrainDark.copy(alpha = 0.35f),
                    radius = w * 0.16f,
                    center = Offset(knotCenterX, knotCenterY),
                    style = Stroke(width = 1.2f)
                )
                drawCircle(
                    color = WoodGrainFiber.copy(alpha = 0.25f),
                    radius = w * 0.28f,
                    center = Offset(knotCenterX, knotCenterY),
                    style = Stroke(width = 1f)
                )
            }
        }

        // Occupied Gemstone or Preview Ghost
        if (cell.occupied && cell.gemType != null) {
            GemBlock(
                gemType = cell.gemType,
                isClearing = cell.isClearing,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
            )
        } else if (isPreview && previewGemType != null) {
            GemBlock(
                gemType = previewGemType,
                isGhost = true,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
            )
        }
    }
}
