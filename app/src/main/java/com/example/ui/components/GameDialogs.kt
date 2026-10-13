package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onHome: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        WoodenDialogFrame(title = "PAUSA") {
            Button(
                onClick = onResume,
                modifier = Modifier
                    .testTag("resume_button")
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WoodGoldAccent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = WoodDarkSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CONTINUAR",
                    color = WoodDarkSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onRestart,
                modifier = Modifier
                    .testTag("restart_button")
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(WoodGoldBorder, WoodGoldAccent)))
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = WoodGoldAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "REINICIAR", color = WoodGoldAccent, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = onHome,
                modifier = Modifier
                    .testTag("home_button")
                    .fillMaxWidth()
            ) {
                Text(text = "Menú Principal", color = WoodTextSecondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun GameOverDialog(
    score: Int,
    highScore: Int,
    isNewRecord: Boolean,
    onRestart: () -> Unit,
    onHome: () -> Unit
) {
    Dialog(onDismissRequest = { /* Modal */ }) {
        WoodenDialogFrame(title = "¡FIN DEL JUEGO!") {
            Text(
                text = "No caben más piezas en el tablero",
                fontSize = 13.sp,
                color = WoodTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Score card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(WoodDarkBackground)
                    .border(1.5.dp, WoodBoardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PUNTUACIÓN FINAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WoodGoldAccent
                    )
                    Text(
                        text = score.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (isNewRecord) {
                        Text(
                            text = "🏆 ¡NUEVO RÉCORD!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TopazHighlight
                        )
                    } else {
                        Text(
                            text = "Récord: $highScore",
                            fontSize = 13.sp,
                            color = WoodTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onRestart,
                modifier = Modifier
                    .testTag("retry_button")
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WoodGoldAccent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = WoodDarkSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "VOLVER A JUGAR",
                    color = WoodDarkSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onHome,
                modifier = Modifier
                    .testTag("game_over_home_button")
                    .fillMaxWidth()
            ) {
                Text(text = "Menú Principal", color = WoodTextSecondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun LevelCompleteDialog(
    score: Int,
    starsEarned: Int,
    nextLevelId: Int?,
    onNextLevel: () -> Unit,
    onLevelMap: () -> Unit
) {
    Dialog(onDismissRequest = { /* Modal */ }) {
        WoodenDialogFrame(title = "¡NIVEL COMPLETADO!") {
            // Stars row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (s in 1..3) {
                    val active = s <= starsEarned
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella",
                        tint = if (active) WoodGoldAccent else Color.DarkGray,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Puntos obtenidos: $score",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (nextLevelId != null && nextLevelId <= 15) {
                Button(
                    onClick = onNextLevel,
                    modifier = Modifier
                        .testTag("next_level_button")
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WoodGoldAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "SIGUIENTE NIVEL",
                        color = WoodDarkSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = onLevelMap,
                modifier = Modifier
                    .testTag("level_map_button")
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(WoodGoldBorder, WoodGoldAccent)))
            ) {
                Text(text = "Mapa de Niveles", color = WoodGoldAccent, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun WoodenDialogFrame(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WoodPlaque, WoodDarkSurface, WoodDarkBackground)
                )
            )
            .border(2.5.dp, WoodGoldBorder, RoundedCornerShape(20.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = WoodGoldAccent,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}
