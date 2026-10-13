package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.AdventureLevels
import com.example.model.LevelConfig
import com.example.ui.theme.*

@Composable
fun LevelSelectScreen(
    unlockedLevel: Int,
    totalStars: Int,
    onLevelSelected: (levelId: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("level_select_back_button")
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(WoodDarkSurface)
                        .border(1.5.dp, WoodGoldBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = WoodGoldAccent
                    )
                }

                Text(
                    text = "MAPA DE NIVELES",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = WoodGoldAccent,
                    letterSpacing = 1.sp
                )

                // Stars badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(WoodDarkSurface)
                        .border(1.dp, WoodGoldBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WoodGoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$totalStars / 45",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Levels grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(AdventureLevels.levels) { level ->
                    LevelNodeCard(
                        level = level,
                        isUnlocked = level.levelId <= unlockedLevel,
                        onClick = {
                            if (level.levelId <= unlockedLevel) {
                                onLevelSelected(level.levelId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelNodeCard(
    level: LevelConfig,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isUnlocked) WoodPlaque else WoodDarkBackground.copy(alpha = 0.7f)
    val borderColor = if (isUnlocked) WoodGoldBorder else WoodBoardBorder.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .testTag("level_node_${level.levelId}")
            .aspectRatio(0.95f)
            .shadow(if (isUnlocked) 8.dp else 2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = isUnlocked, onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (!isUnlocked) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bloqueado",
                    tint = WoodTextSecondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Nivel ${level.levelId}",
                    fontSize = 12.sp,
                    color = WoodTextSecondary.copy(alpha = 0.6f)
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Level number
                Text(
                    text = "${level.levelId}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = WoodGoldAccent
                )

                // Grid size tag
                Text(
                    text = "${level.rows}x${level.cols}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WoodTextSecondary
                )

                // Target points
                Text(
                    text = "${level.targetScore} pts",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                // Star icons
                Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WoodGoldAccent,
                        modifier = Modifier.size(13.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WoodGoldAccent,
                        modifier = Modifier.size(13.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WoodGoldAccent,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
