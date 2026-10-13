package com.example.data

import android.content.Context
import android.content.SharedPreferences

class GameRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("block_blast_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CLASSIC_HIGH_SCORE = "classic_high_score"
        private const val KEY_UNLOCKED_LEVEL = "unlocked_level"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_HAPTIC_ENABLED = "haptic_enabled"
        private const val PREFIX_LEVEL_STARS = "level_stars_"
        private const val PREFIX_LEVEL_SCORE = "level_score_"
    }

    fun getClassicHighScore(): Int {
        return prefs.getInt(KEY_CLASSIC_HIGH_SCORE, 0)
    }

    fun saveClassicHighScore(score: Int) {
        val current = getClassicHighScore()
        if (score > current) {
            prefs.edit().putInt(KEY_CLASSIC_HIGH_SCORE, score).apply()
        }
    }

    fun getUnlockedLevel(): Int {
        return prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
    }

    fun unlockNextLevel(completedLevel: Int) {
        val nextLevel = completedLevel + 1
        val current = getUnlockedLevel()
        if (nextLevel > current) {
            prefs.edit().putInt(KEY_UNLOCKED_LEVEL, nextLevel).apply()
        }
    }

    fun getLevelStars(levelId: Int): Int {
        return prefs.getInt("$PREFIX_LEVEL_STARS$levelId", 0)
    }

    fun saveLevelStars(levelId: Int, stars: Int) {
        val current = getLevelStars(levelId)
        if (stars > current) {
            prefs.edit().putInt("$PREFIX_LEVEL_STARS$levelId", stars).apply()
        }
    }

    fun getLevelHighScore(levelId: Int): Int {
        return prefs.getInt("$PREFIX_LEVEL_SCORE$levelId", 0)
    }

    fun saveLevelScore(levelId: Int, score: Int) {
        val current = getLevelHighScore(levelId)
        if (score > current) {
            prefs.edit().putInt("$PREFIX_LEVEL_SCORE$levelId", score).apply()
        }
    }

    fun getTotalStarsEarned(): Int {
        var total = 0
        for (i in 1..15) {
            total += getLevelStars(i)
        }
        return total
    }

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }

    fun isHapticEnabled(): Boolean {
        return prefs.getBoolean(KEY_HAPTIC_ENABLED, true)
    }

    fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTIC_ENABLED, enabled).apply()
    }
}
