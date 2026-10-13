package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GemType
import com.example.ui.components.GemBlock
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun HomeScreen(
    highScore: Int,
    totalStars: Int,
    unlockedLevel: Int,
    onStartClassic: () -> Unit,
    onGoToAdventure: () -> Unit,
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top settings button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onGoToSettings,
                    modifier = Modifier
                        .testTag("settings_button")
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(WoodDarkSurface)
                        .border(1.5.dp, WoodGoldBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = WoodGoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Game Logo Title Plaque with Dynamic Animations
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Animated floating gem cluster above logo
                AnimatedFloatingGemCluster()

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Animated Title Banner with Shimmer & Twinkle Stars
                AnimatedTitleBanner()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // High Score & Adventure Stats Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Classic High score
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(WoodDarkSurface)
                        .border(1.5.dp, WoodBoardBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🏆 RÉCORD CLÁSICO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WoodTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = highScore.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = WoodGoldAccent
                        )
                    }
                }

                // Stars earned
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(WoodDarkSurface)
                        .border(1.5.dp, WoodBoardBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "★ ESTRELLAS TOTALES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WoodTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalStars / 45",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = WoodGoldAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Action Buttons with subtle pulse invitation
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Modo Clásico (Play button)
                val pulseTransition = rememberInfiniteTransition(label = "pulse_play")
                val playScale by pulseTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.03f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(900, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "play_scale"
                )

                Button(
                    onClick = onStartClassic,
                    modifier = Modifier
                        .testTag("play_classic_button")
                        .fillMaxWidth()
                        .height(60.dp)
                        .scale(playScale)
                        .shadow(14.dp, RoundedCornerShape(16.dp), spotColor = WoodGoldAccent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WoodGoldAccent
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = WoodDarkSurface,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "MODO CLÁSICO",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = WoodDarkSurface,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Modo Aventura (Levels map)
                Button(
                    onClick = onGoToAdventure,
                    modifier = Modifier
                        .testTag("play_adventure_button")
                        .fillMaxWidth()
                        .height(58.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WoodPlaque
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(WoodGoldBorder, WoodGoldAccent))
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = WoodGoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "MODO AVENTURA (Nivel $unlockedLevel)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = WoodGoldAccent,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer tip
            Text(
                text = "✨ ¡Destruye varias líneas seguidas para activar COMBOS masivos!",
                fontSize = 12.sp,
                color = WoodTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Animated floating gem cluster above the game logo with:
 * - Harmonic wave floating effect (sine wave bouncing)
 * - Dynamic tilt rotation
 * - Pulsing colored neon halo shadows
 * - Glimmering diamond sparkles
 */
@Composable
private fun AnimatedFloatingGemCluster() {
    val infiniteTransition = rememberInfiniteTransition(label = "gem_cluster_anim")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283f, // 2*PI
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time_anim"
    )

    val gems = listOf(
        Pair(GemType.RUBY, 30.dp),
        Pair(GemType.SAPPHIRE, 34.dp),
        Pair(GemType.EMERALD, 40.dp),
        Pair(GemType.TOPAZ, 34.dp),
        Pair(GemType.AMETHYST, 30.dp)
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        gems.forEachIndexed { index, (gem, size) ->
            // Phase offset creates a fluid harmonic wave motion across the gems
            val phaseOffset = index * 1.15f
            val floatOffset = (sin(time + phaseOffset) * 8f).dp
            val tiltAngle = sin(time * 0.9f + phaseOffset) * 9f
            val scalePulse = 1f + sin(time * 1.1f + phaseOffset) * 0.07f

            Box(
                modifier = Modifier
                    .offset(y = floatOffset)
                    .rotate(tiltAngle)
                    .scale(scalePulse)
                    .shadow(12.dp, CircleShape, spotColor = gem.primaryColor),
                contentAlignment = Alignment.Center
            ) {
                GemBlock(gemType = gem, size = size)

                // Twinkling sparkle star over the jewel
                val sparkleAlpha = (sin(time * 2f + phaseOffset) * 0.5f + 0.5f).coerceIn(0.2f, 1f)
                Text(
                    text = "✦",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = sparkleAlpha),
                    modifier = Modifier.align(Alignment.TopEnd).padding(2.dp)
                )
            }
        }
    }
}

/**
 * Animated Title Banner with:
 * - Moving golden shimmer sweep across text
 * - Breathing plaque frame with gold trim
 * - Twinkling corner stars
 */
@Composable
private fun AnimatedTitleBanner() {
    val infiniteTransition = rememberInfiniteTransition(label = "title_shimmer_anim")

    // Moving shimmer sweep offset (0f to 1000f)
    val shimmerX by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_x"
    )

    // Breathing scale of the plaque
    val bannerScale by infiniteTransition.animateFloat(
        initialValue = 0.99f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "banner_scale"
    )

    // Twinkling stars alpha
    val starAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_alpha"
    )

    Box(
        modifier = Modifier
            .scale(bannerScale)
            .shadow(20.dp, RoundedCornerShape(26.dp), spotColor = WoodGoldAccent)
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WoodPlaque,
                        WoodDarkSurface,
                        WoodBoardBorder
                    )
                )
            )
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        WoodGoldBorder,
                        WoodGoldAccent,
                        WoodBoardBorder,
                        WoodGoldAccent
                    ),
                    start = Offset(shimmerX - 200f, 0f),
                    end = Offset(shimmerX + 200f, 200f)
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(horizontal = 26.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "✨",
                fontSize = 18.sp,
                color = WoodGoldAccent.copy(alpha = starAlpha)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Shimmering Golden Title
                Text(
                    text = "BLOCK BLAST",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    color = WoodGoldAccent
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "GEMS & WOOD PUZZLE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center,
                    color = WoodTextPrimary
                )
            }

            Text(
                text = "✨",
                fontSize = 18.sp,
                color = WoodGoldAccent.copy(alpha = starAlpha)
            )
        }
    }
}
