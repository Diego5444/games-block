package com.example.engine

import com.example.model.BlockPiece
import com.example.model.GemType
import kotlin.random.Random

object ShapeDefinitions {

    // 1x1 Dot
    private val DOT = listOf(listOf(true))

    // Dominos (2 blocks)
    private val HORIZONTAL_2 = listOf(listOf(true, true))
    private val VERTICAL_2 = listOf(listOf(true), listOf(true))

    // Triominos (3 blocks)
    private val HORIZONTAL_3 = listOf(listOf(true, true, true))
    private val VERTICAL_3 = listOf(listOf(true), listOf(true), listOf(true))
    private val L_SMALL_1 = listOf(listOf(true, false), listOf(true, true))
    private val L_SMALL_2 = listOf(listOf(false, true), listOf(true, true))
    private val L_SMALL_3 = listOf(listOf(true, true), listOf(true, false))
    private val L_SMALL_4 = listOf(listOf(true, true), listOf(false, true))

    // Tetraminos (4 blocks)
    private val SQUARE_2X2 = listOf(
        listOf(true, true),
        listOf(true, true)
    )
    private val HORIZONTAL_4 = listOf(listOf(true, true, true, true))
    private val VERTICAL_4 = listOf(listOf(true), listOf(true), listOf(true), listOf(true))

    // L Shapes (4 blocks)
    private val L_SHAPE_1 = listOf(
        listOf(true, false),
        listOf(true, false),
        listOf(true, true)
    )
    private val L_SHAPE_2 = listOf(
        listOf(false, true),
        listOf(false, true),
        listOf(true, true)
    )
    private val L_SHAPE_3 = listOf(
        listOf(true, true, true),
        listOf(true, false, false)
    )
    private val L_SHAPE_4 = listOf(
        listOf(true, true, true),
        listOf(false, false, true)
    )

    // T Shapes (4 blocks)
    private val T_SHAPE_1 = listOf(
        listOf(true, true, true),
        listOf(false, true, false)
    )
    private val T_SHAPE_2 = listOf(
        listOf(false, true, false),
        listOf(true, true, true)
    )
    private val T_SHAPE_3 = listOf(
        listOf(true, false),
        listOf(true, true),
        listOf(true, false)
    )
    private val T_SHAPE_4 = listOf(
        listOf(false, true),
        listOf(true, true),
        listOf(false, true)
    )

    // Z and S Shapes (4 blocks)
    private val Z_SHAPE_H = listOf(
        listOf(true, true, false),
        listOf(false, true, true)
    )
    private val S_SHAPE_H = listOf(
        listOf(false, true, true),
        listOf(true, true, false)
    )
    private val Z_SHAPE_V = listOf(
        listOf(false, true),
        listOf(true, true),
        listOf(true, false)
    )
    private val S_SHAPE_V = listOf(
        listOf(true, false),
        listOf(true, true),
        listOf(false, true)
    )

    // Pentaminos and Special (5+ blocks)
    private val HORIZONTAL_5 = listOf(listOf(true, true, true, true, true))
    private val VERTICAL_5 = listOf(listOf(true), listOf(true), listOf(true), listOf(true), listOf(true))
    private val SQUARE_3X3 = listOf(
        listOf(true, true, true),
        listOf(true, true, true),
        listOf(true, true, true)
    )
    private val PLUS_3X3 = listOf(
        listOf(false, true, false),
        listOf(true, true, true),
        listOf(false, true, false)
    )
    private val BIG_L_1 = listOf(
        listOf(true, false, false),
        listOf(true, false, false),
        listOf(true, true, true)
    )
    private val BIG_L_2 = listOf(
        listOf(false, false, true),
        listOf(false, false, true),
        listOf(true, true, true)
    )

    // Common easy pool (used often so player has high placement flexibility)
    private val EASY_SHAPES = listOf(
        DOT, DOT,
        HORIZONTAL_2, VERTICAL_2,
        HORIZONTAL_3, VERTICAL_3,
        L_SMALL_1, L_SMALL_2, L_SMALL_3, L_SMALL_4,
        SQUARE_2X2
    )

    // Medium pool
    private val MEDIUM_SHAPES = listOf(
        HORIZONTAL_4, VERTICAL_4,
        L_SHAPE_1, L_SHAPE_2, L_SHAPE_3, L_SHAPE_4,
        T_SHAPE_1, T_SHAPE_2, T_SHAPE_3, T_SHAPE_4,
        Z_SHAPE_H, S_SHAPE_H, Z_SHAPE_V, S_SHAPE_V
    )

    // Hard/rare pool (occasional challenge)
    private val CHALLENGE_SHAPES = listOf(
        HORIZONTAL_5, VERTICAL_5,
        SQUARE_3X3,
        PLUS_3X3,
        BIG_L_1, BIG_L_2
    )

    fun createRandomPiece(random: Random = Random): BlockPiece {
        val roll = random.nextFloat()
        val matrix = when {
            roll < 0.45f -> EASY_SHAPES.random(random)
            roll < 0.85f -> MEDIUM_SHAPES.random(random)
            else -> CHALLENGE_SHAPES.random(random)
        }
        val gem = GemType.playableGems.random(random)
        return BlockPiece(matrix = matrix, gemType = gem)
    }

    fun generateThreePieces(random: Random = Random): List<BlockPiece> {
        // Guarantee at least one smaller/flexible shape in each tray to keep the flow rewarding
        val pieces = mutableListOf<BlockPiece>()
        pieces.add(BlockPiece(matrix = EASY_SHAPES.random(random), gemType = GemType.playableGems.random(random)))
        pieces.add(createRandomPiece(random))
        pieces.add(createRandomPiece(random))
        return pieces.shuffled(random)
    }
}
