package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GemType
import com.example.ui.theme.*

@Composable
fun GemBlock(
    gemType: GemType,
    modifier: Modifier = Modifier,
    isGhost: Boolean = false,
    isClearing: Boolean = false,
    size: Dp = 36.dp
) {
    val scale = if (isClearing) {
        val transition = rememberInfiniteTransition(label = "clearing")
        val animScale by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(120, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "clearing_scale"
        )
        animScale
    } else 1f

    val alpha = if (isGhost) 0.55f else 1f

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .padding(1.5.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        gemType.highlightColor.copy(alpha = alpha),
                        gemType.primaryColor.copy(alpha = alpha),
                        gemType.shadowColor.copy(alpha = alpha)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .border(
                width = if (isGhost) 1.5.dp else 1.dp,
                color = if (isGhost) Color.White.copy(alpha = 0.8f) else gemType.highlightColor.copy(alpha = alpha * 0.9f),
                shape = RoundedCornerShape(6.dp)
            )
            .drawBehind {
                if (!isGhost && gemType != GemType.OBSTACLE) {
                    // Specular light facet triangle (top-left)
                    val facetPath = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(this@drawBehind.size.width * 0.7f, 0f)
                        lineTo(0f, this@drawBehind.size.height * 0.7f)
                        close()
                    }
                    drawPath(
                        path = facetPath,
                        color = Color.White.copy(alpha = 0.35f)
                    )

                    // Small center sparkle glint
                    val centerX = this.size.width * 0.32f
                    val centerY = this.size.height * 0.32f
                    drawCircle(
                        color = Color.White.copy(alpha = 0.85f),
                        radius = this.size.width * 0.10f,
                        center = Offset(centerX, centerY)
                    )
                } else if (gemType == GemType.OBSTACLE) {
                    // Wood grain lines
                    drawLine(
                        color = WoodDarkBackground.copy(alpha = 0.6f),
                        start = Offset(4f, 8f),
                        end = Offset(this.size.width - 4f, 8f),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = WoodDarkBackground.copy(alpha = 0.6f),
                        start = Offset(4f, this.size.height - 8f),
                        end = Offset(this.size.width - 4f, this.size.height - 8f),
                        strokeWidth = 2f
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (gemType == GemType.FROZEN) {
            Text(
                text = "❄",
                fontSize = (size.value * 0.45f).sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
