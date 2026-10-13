package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlockBlastColorScheme = darkColorScheme(
    primary = WoodGoldAccent,
    onPrimary = Color(0xFF26150D),
    secondary = SapphireHighlight,
    onSecondary = Color(0xFF0D47A1),
    tertiary = RubyHighlight,
    onTertiary = Color.White,
    background = WoodDarkBackground,
    onBackground = WoodTextPrimary,
    surface = WoodDarkSurface,
    onSurface = WoodTextPrimary,
    surfaceVariant = WoodPlaque,
    onSurfaceVariant = WoodTextSecondary,
    outline = WoodGoldBorder
)

@Composable
fun BlockBlastTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlockBlastColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    BlockBlastTheme(content = content)
}
