package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Screen
import com.example.ui.BlockBlastViewModel
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LevelSelectScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BlockBlastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlockBlastTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BlockBlastApp()
                }
            }
        }
    }
}

@Composable
fun BlockBlastApp(
    viewModel: BlockBlastViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (state.currentScreen) {
        Screen.HOME -> {
            HomeScreen(
                highScore = state.highScore,
                totalStars = state.totalStars,
                unlockedLevel = state.unlockedLevel,
                onStartClassic = { viewModel.startClassicGame() },
                onGoToAdventure = { viewModel.navigateTo(Screen.LEVEL_SELECT) },
                onGoToSettings = { viewModel.navigateTo(Screen.SETTINGS) }
            )
        }
        Screen.LEVEL_SELECT -> {
            LevelSelectScreen(
                unlockedLevel = state.unlockedLevel,
                totalStars = state.totalStars,
                onLevelSelected = { levelId ->
                    viewModel.startAdventureLevel(levelId)
                },
                onBack = { viewModel.navigateTo(Screen.HOME) }
            )
        }
        Screen.GAMEPLAY -> {
            GamePlayScreen(viewModel = viewModel)
        }
        Screen.SETTINGS -> {
            SettingsScreen(
                soundEnabled = state.soundEnabled,
                hapticEnabled = state.hapticEnabled,
                onToggleSound = { viewModel.toggleSound() },
                onToggleHaptic = { viewModel.toggleHaptic() },
                onBack = { viewModel.navigateTo(Screen.HOME) }
            )
        }
    }
}
