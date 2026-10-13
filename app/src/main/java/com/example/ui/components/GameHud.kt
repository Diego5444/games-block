package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ComboFeedback
import com.example.model.GameMode
import com.example.model.LevelConfig
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun TopGameHud(
    score: Int,
    highScore: Int,
    gameMode: GameMode,
    levelConfig: LevelConfig?,
    movesLeft: Int?,
    comboStreak: Int,
    soundEnabled: Boolean,
    onPauseClick: () -> Unit,
    onSoundToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // Top control buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pause button
            IconButton(
                onClick = onPauseClick,
                modifier = Modifier
                    .testTag("pause_button")
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(WoodDarkSurface)
                    .border(1.5.dp, WoodGoldBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Pausa",
                    tint = WoodGoldAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Streak Flame badge if active
            if (comboStreak > 0) {
                StreakFlameBadge(comboStreak = comboStreak)
            }

            // Sound toggle
            IconButton(
                onClick = onSoundToggle,
                modifier = Modifier
                    .testTag("sound_toggle_button")
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(WoodDarkSurface)
                    .border(1.5.dp, WoodBoardBorder, CircleShape)
            ) {
                Icon(
                    imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                    contentDescription = "Sonido",
                    tint = if (soundEnabled) WoodGoldAccent else WoodTextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Mode specific stats
        when (gameMode) {
            is GameMode.Classic -> {
                ClassicScoreHeader(score = score, highScore = highScore)
            }
            is GameMode.Adventure -> {
                if (levelConfig != null) {
                    AdventureScoreHeader(
                        levelConfig = levelConfig,
                        currentScore = score,
                        movesLeft = movesLeft
                    )
                }
            }
        }
    }
}

@Composable
private fun ClassicScoreHeader(score: Int, highScore: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Current score plaque
        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(6.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(WoodPlaque, WoodDarkSurface)
                    )
                )
                .border(2.dp, WoodGoldBorder, RoundedCornerShape(14.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "PUNTUACIÓN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WoodGoldAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = score.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        // High score plaque
        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(6.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(WoodDarkSurface, WoodDarkBackground)
                    )
                )
                .border(1.5.dp, WoodBoardBorder, RoundedCornerShape(14.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "RÉCORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WoodTextSecondary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = highScore.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = WoodGoldAccent
                )
            }
        }
    }
}

@Composable
private fun AdventureScoreHeader(
    levelConfig: LevelConfig,
    currentScore: Int,
    movesLeft: Int?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WoodPlaque, WoodDarkSurface)
                )
            )
            .border(2.dp, WoodGoldBorder, RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = levelConfig.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WoodGoldAccent
                )

                if (movesLeft != null) {
                    Text(
                        text = "Movimientos: $movesLeft",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (movesLeft <= 5) RubyHighlight else WoodTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Star progress bar (completes 100% when reaching targetScore)
            val targetScore = levelConfig.targetScore.coerceAtLeast(1)
            val rawProgress = (currentScore.toFloat() / targetScore).coerceIn(0f, 1f)
            val animatedProgress by animateFloatAsState(
                targetValue = rawProgress,
                animationSpec = tween(350, easing = FastOutSlowInEasing),
                label = "progress_anim"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(WoodDarkBackground)
                    .border(1.dp, WoodGoldBorder.copy(alpha = 0.5f), RoundedCornerShape(7.dp))
            ) {
                // Animated fill
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    TopazPrimary,
                                    WoodGoldAccent,
                                    WoodGoldBorder
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val percent = (rawProgress * 100).toInt()
                Text(
                    text = "$currentScore / ${levelConfig.targetScore} PTS ($percent%)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (rawProgress >= 1f) WoodGoldAccent else Color.White
                )

                // 3 stars indicator
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    val star1 = currentScore >= levelConfig.star1Score
                    val star2 = currentScore >= levelConfig.star2Score
                    val star3 = currentScore >= levelConfig.targetScore

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella 1",
                        tint = if (star1) WoodGoldAccent else Color.DarkGray,
                        modifier = Modifier.size(19.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella 2",
                        tint = if (star2) WoodGoldAccent else Color.DarkGray,
                        modifier = Modifier.size(19.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella 3",
                        tint = if (star3) WoodGoldAccent else Color.DarkGray,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StreakFlameBadge(comboStreak: Int) {
    val transition = rememberInfiniteTransition(label = "flame")
    val pulseScale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )

    Box(
        modifier = Modifier
            .scale(pulseScale)
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = TopazPrimary)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(RubyPrimary, TopazPrimary, WoodGoldAccent)
                )
            )
            .border(1.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "🔥", fontSize = 16.sp)
            Text(
                text = "RACHA x$comboStreak",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
fun ComboBannerOverlay(
    feedback: ComboFeedback?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = feedback != null,
        enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
        exit = scaleOut() + fadeOut(),
        modifier = modifier
    ) {
        if (feedback != null) {
            LaunchedEffect(feedback.id) {
                delay(1200)
                onDismiss()
            }

            Box(
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = WoodGoldAccent)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                RubyPrimary,
                                WoodPlaque,
                                WoodDarkBackground
                            )
                        )
                    )
                    .border(2.5.dp, WoodGoldAccent, RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = feedback.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = WoodGoldAccent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "+${feedback.bonusPoints} PTS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
