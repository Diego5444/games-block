package com.example.engine

import com.example.model.GemType
import com.example.model.LevelConfig

object AdventureLevels {

    val levels: List<LevelConfig> = listOf(
        // Level 1: Introducción (7x7) - Muy fácil
        LevelConfig(
            levelId = 1,
            title = "Nivel 1: Primeros Pasos",
            description = "Destruye líneas hasta llenar la barra con 300 puntos.",
            rows = 7,
            cols = 7,
            targetScore = 300,
            star1Score = 100,
            star2Score = 200,
            star3Score = 300
        ),

        // Level 2: Líneas Dobles (7x7) - Muy fácil
        LevelConfig(
            levelId = 2,
            title = "Nivel 2: Doble Explosión",
            description = "Destruye filas y columnas para sumar 500 puntos.",
            rows = 7,
            cols = 7,
            targetScore = 500,
            star1Score = 200,
            star2Score = 350,
            star3Score = 500
        ),

        // Level 3: Racha de Combos (7x7) - Fácil
        LevelConfig(
            levelId = 3,
            title = "Nivel 3: El Arte del Combo",
            description = "Encadena líneas en turnos consecutivos hasta 750 puntos.",
            rows = 7,
            cols = 7,
            targetScore = 750,
            star1Score = 250,
            star2Score = 500,
            star3Score = 750
        ),

        // Level 4: Tablero Completo 8x8 - Fácil
        LevelConfig(
            levelId = 4,
            title = "Nivel 4: El Gran Tablero",
            description = "Tablero de 8x8. Alcanza 1000 puntos completando líneas.",
            rows = 8,
            cols = 8,
            targetScore = 1000,
            star1Score = 350,
            star2Score = 650,
            star3Score = 1000
        ),

        // Level 5: Obstáculos de Madera en Esquinas - Fácil
        LevelConfig(
            levelId = 5,
            title = "Nivel 5: Madera Tallada",
            description = "Obstáculos en las esquinas. Despeja líneas hasta 1300 pts.",
            rows = 8,
            cols = 8,
            targetScore = 1300,
            initialObstacles = listOf(
                Pair(0, 0), Pair(0, 7),
                Pair(7, 0), Pair(7, 7)
            ),
            star1Score = 450,
            star2Score = 850,
            star3Score = 1300
        ),

        // Level 6: Límite de Movimientos - Fácil a moderado
        LevelConfig(
            levelId = 6,
            title = "Nivel 6: Movimientos Contados",
            description = "Consigue 1600 puntos en un máximo de 30 movimientos.",
            rows = 8,
            cols = 8,
            targetScore = 1600,
            maxMoves = 30,
            star1Score = 550,
            star2Score = 1050,
            star3Score = 1600
        ),

        // Level 7: Gemas de Hielo en Centro - Moderado
        LevelConfig(
            levelId = 7,
            title = "Nivel 7: Gemas de Hielo",
            description = "Gemas de hielo en el centro. Incorpóralas a tus líneas para 2000 pts.",
            rows = 8,
            cols = 8,
            targetScore = 2000,
            initialGems = listOf(
                Triple(3, 3, GemType.FROZEN),
                Triple(3, 4, GemType.FROZEN),
                Triple(4, 3, GemType.FROZEN),
                Triple(4, 4, GemType.FROZEN)
            ),
            star1Score = 700,
            star2Score = 1300,
            star3Score = 2000
        ),

        // Level 8: Cruz Central con Límite - Moderado
        LevelConfig(
            levelId = 8,
            title = "Nivel 8: La Cruz del Bosque",
            description = "Obstáculos centrales y 28 movimientos. Llega a 2400 pts.",
            rows = 8,
            cols = 8,
            targetScore = 2400,
            maxMoves = 28,
            initialObstacles = listOf(
                Pair(3, 2), Pair(3, 5),
                Pair(4, 2), Pair(4, 5)
            ),
            star1Score = 800,
            star2Score = 1600,
            star3Score = 2400
        ),

        // Level 9: Mina de Zafiros - Moderado
        LevelConfig(
            levelId = 9,
            title = "Nivel 9: Mina de Zafiros",
            description = "Gemas dispersas en diagonales. Completa líneas hasta 2800 puntos.",
            rows = 8,
            cols = 8,
            targetScore = 2800,
            initialGems = listOf(
                Triple(1, 1, GemType.SAPPHIRE),
                Triple(1, 6, GemType.SAPPHIRE),
                Triple(6, 1, GemType.RUBY),
                Triple(6, 6, GemType.RUBY),
                Triple(3, 3, GemType.EMERALD),
                Triple(4, 4, GemType.EMERALD)
            ),
            star1Score = 1000,
            star2Score = 1900,
            star3Score = 2800
        ),

        // Level 10: Fin de la Primera Etapa - Desafío moderado
        LevelConfig(
            levelId = 10,
            title = "Nivel 10: Precisión del Bosque",
            description = "Alcanza 3200 puntos en 28 movimientos. Los combos multiplican tus puntos.",
            rows = 8,
            cols = 8,
            targetScore = 3200,
            maxMoves = 28,
            initialObstacles = listOf(
                Pair(2, 2), Pair(2, 5), Pair(5, 2), Pair(5, 5)
            ),
            star1Score = 1100,
            star2Score = 2100,
            star3Score = 3200
        ),

        // Level 11: Laberinto de Madera (Complicado)
        LevelConfig(
            levelId = 11,
            title = "Nivel 11: Laberinto de Madera",
            description = "Obstáculos distribuidos estratégicamente. Llena la barra con 3600 pts.",
            rows = 8,
            cols = 8,
            targetScore = 3600,
            initialObstacles = listOf(
                Pair(1, 3), Pair(2, 4), Pair(5, 3), Pair(6, 4)
            ),
            star1Score = 1200,
            star2Score = 2400,
            star3Score = 3600
        ),

        // Level 12: Desafío de Rubíes
        LevelConfig(
            levelId = 12,
            title = "Nivel 12: Corona de Rubíes",
            description = "Supera 4200 puntos en 28 movimientos con obstáculos periféricos.",
            rows = 8,
            cols = 8,
            targetScore = 4200,
            maxMoves = 28,
            initialGems = listOf(
                Triple(2, 2, GemType.RUBY), Triple(2, 5, GemType.RUBY),
                Triple(5, 2, GemType.TOPAZ), Triple(5, 5, GemType.TOPAZ)
            ),
            initialObstacles = listOf(
                Pair(0, 3), Pair(0, 4), Pair(7, 3), Pair(7, 4)
            ),
            star1Score = 1400,
            star2Score = 2800,
            star3Score = 4200
        ),

        // Level 13: La Fortaleza de Hielo
        LevelConfig(
            levelId = 13,
            title = "Nivel 13: Fortaleza Helada",
            description = "Gemas congeladas rodeadas de madera fija. Alcanza 4800 puntos.",
            rows = 8,
            cols = 8,
            targetScore = 4800,
            initialGems = listOf(
                Triple(2, 3, GemType.FROZEN), Triple(2, 4, GemType.FROZEN),
                Triple(5, 3, GemType.FROZEN), Triple(5, 4, GemType.FROZEN)
            ),
            initialObstacles = listOf(
                Pair(1, 1), Pair(1, 6), Pair(6, 1), Pair(6, 6)
            ),
            star1Score = 1600,
            star2Score = 3200,
            star3Score = 4800
        ),

        // Level 14: Maestro Artesano
        LevelConfig(
            levelId = 14,
            title = "Nivel 14: Maestro Artesano",
            description = "Alcanza 5500 puntos en un máximo de 30 movimientos.",
            rows = 8,
            cols = 8,
            targetScore = 5500,
            maxMoves = 30,
            initialObstacles = listOf(
                Pair(0, 2), Pair(0, 5), Pair(7, 2), Pair(7, 5),
                Pair(3, 0), Pair(4, 0), Pair(3, 7), Pair(4, 7)
            ),
            star1Score = 1800,
            star2Score = 3600,
            star3Score = 5500
        ),

        // Level 15: Gran Final de Gemas
        LevelConfig(
            levelId = 15,
            title = "Nivel 15: Leyenda de Gemas",
            description = "El desafío definitivo. Llena completamente la barra con 6500 puntos para la victoria total.",
            rows = 8,
            cols = 8,
            targetScore = 6500,
            maxMoves = 32,
            initialGems = listOf(
                Triple(0, 0, GemType.DIAMOND), Triple(7, 7, GemType.DIAMOND),
                Triple(0, 7, GemType.AMETHYST), Triple(7, 0, GemType.AMETHYST),
                Triple(3, 3, GemType.CITRINE), Triple(4, 4, GemType.CITRINE)
            ),
            star1Score = 2200,
            star2Score = 4300,
            star3Score = 6500
        )
    )

    fun getLevel(levelId: Int): LevelConfig {
        return levels.firstOrNull { it.levelId == levelId } ?: levels.first()
    }
}
