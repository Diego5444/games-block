package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    soundEnabled: Boolean,
    hapticEnabled: Boolean,
    onToggleSound: () -> Unit,
    onToggleHaptic: () -> Unit,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("settings_back_button")
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

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "AJUSTES Y AYUDA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = WoodGoldAccent,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Settings card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(WoodPlaque)
                    .border(2.dp, WoodGoldBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Sound FX
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Efectos de Sonido",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Tonos de colocación, explosión y fanfarrias",
                                fontSize = 12.sp,
                                color = WoodTextSecondary
                            )
                        }
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { onToggleSound() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = WoodDarkSurface,
                                checkedTrackColor = WoodGoldAccent,
                                uncheckedThumbColor = WoodTextSecondary,
                                uncheckedTrackColor = WoodDarkBackground
                            )
                        )
                    }

                    HorizontalDivider(color = WoodBoardBorder)

                    // Haptic Feedback
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Vibración Háptica",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Respuesta táctil al encajar piezas y combos",
                                fontSize = 12.sp,
                                color = WoodTextSecondary
                            )
                        }
                        Switch(
                            checked = hapticEnabled,
                            onCheckedChange = { onToggleHaptic() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = WoodDarkSurface,
                                checkedTrackColor = WoodGoldAccent,
                                uncheckedThumbColor = WoodTextSecondary,
                                uncheckedTrackColor = WoodDarkBackground
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // How to Play Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(WoodDarkSurface)
                    .border(1.5.dp, WoodBoardBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "CÓMO JUGAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = WoodGoldAccent,
                        letterSpacing = 1.sp
                    )

                    InstructionItem(
                        number = "1",
                        title = "Arrastra y Suelta",
                        body = "Toma las 3 piezas poliominó de gemas de la bandeja inferior y colócalas en los espacios libres del tablero de madera."
                    )

                    InstructionItem(
                        number = "2",
                        title = "Limpia Filas y Columnas",
                        body = "Cuando completes una fila horizontal o columna vertical entera, ¡las gemas explotarán liberando espacio y otorgando puntos!"
                    )

                    InstructionItem(
                        number = "3",
                        title = "Multiplicadores de Combo 🔥",
                        body = "¡Destruye líneas en turnos consecutivos para activar la RACHA DE COMBOS (x2, x3, x4...) y multiplicar exponencialmente tu puntuación!"
                    )

                    InstructionItem(
                        number = "4",
                        title = "Doble y Triple Blast",
                        body = "Si destruyes 2 o más líneas con una sola pieza, recibirás un bono masivo de puntos instantáneos."
                    )

                    InstructionItem(
                        number = "5",
                        title = "Modo Aventura",
                        body = "Avanza por 15 niveles desafiantes con tableros dinámicos (7x7, 8x8), obstáculos de madera fijos y gemas congeladas que debes incorporar a tus líneas."
                    )
                }
            }
        }
    }
}

@Composable
private fun InstructionItem(
    number: String,
    title: String,
    body: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(WoodGoldAccent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = WoodDarkSurface
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = body,
                fontSize = 12.sp,
                color = WoodTextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}
