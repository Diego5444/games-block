package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import java.util.UUID

enum class GemType(
    val primaryColor: Color,
    val highlightColor: Color,
    val shadowColor: Color
) {
    RUBY(RubyPrimary, RubyHighlight, RubyShadow),
    SAPPHIRE(SapphirePrimary, SapphireHighlight, SapphireShadow),
    EMERALD(EmeraldPrimary, EmeraldHighlight, EmeraldShadow),
    TOPAZ(TopazPrimary, TopazHighlight, TopazShadow),
    AMETHYST(AmethystPrimary, AmethystHighlight, AmethystShadow),
    DIAMOND(DiamondPrimary, DiamondHighlight, DiamondShadow),
    CITRINE(CitrinePrimary, CitrineHighlight, CitrineShadow),
    OBSTACLE(ObstacleWood, ObstacleWoodBorder, WoodDarkBackground),
    FROZEN(FrozenIce, FrozenIceBorder, SapphireShadow);

    companion object {
        val playableGems = listOf(RUBY, SAPPHIRE, EMERALD, TOPAZ, AMETHYST, DIAMOND, CITRINE)
    }
}

data class BlockPiece(
    val id: String = UUID.randomUUID().toString(),
    val matrix: List<List<Boolean>>,
    val gemType: GemType
) {
    val rows: Int get() = matrix.size
    val cols: Int get() = matrix.firstOrNull()?.size ?: 0
    val blockCount: Int get() = matrix.sumOf { row -> row.count { it } }
}

data class BoardCell(
    val occupied: Boolean = false,
    val gemType: GemType? = null,
    val isObstacle: Boolean = false,
    val isTargetGoal: Boolean = false,
    val isClearing: Boolean = false
)

sealed class GameMode {
    object Classic : GameMode()
    data class Adventure(val levelId: Int) : GameMode()
}

data class LevelConfig(
    val levelId: Int,
    val title: String,
    val description: String,
    val rows: Int = 8,
    val cols: Int = 8,
    val targetScore: Int,
    val maxMoves: Int? = null,
    val initialObstacles: List<Pair<Int, Int>> = emptyList(),
    val initialGems: List<Triple<Int, Int, GemType>> = emptyList(),
    val star1Score: Int,
    val star2Score: Int,
    val star3Score: Int
)

data class ComboFeedback(
    val linesCleared: Int,
    val streakCount: Int,
    val multiplier: Int,
    val bonusPoints: Int,
    val title: String,
    val id: Long = System.currentTimeMillis()
)

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val alpha: Float = 1f
)

enum class Screen {
    HOME,
    LEVEL_SELECT,
    GAMEPLAY,
    SETTINGS
}
