package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundEffects(private val context: Context) {

    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 70)
    } catch (_: Exception) {
        null
    }

    private val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (_: Exception) {
        null
    }

    private val scope = CoroutineScope(Dispatchers.Default)

    fun playPickUp(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        } catch (_: Exception) {}
    }

    fun playPlace(soundEnabled: Boolean, hapticEnabled: Boolean) {
        if (soundEnabled) {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
            } catch (_: Exception) {}
        }
        vibrate(hapticEnabled, 25)
    }

    fun playLineClear(soundEnabled: Boolean, hapticEnabled: Boolean) {
        if (soundEnabled) {
            scope.launch {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 70)
                    delay(80)
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 70)
                } catch (_: Exception) {}
            }
        }
        vibrate(hapticEnabled, 50)
    }

    fun playCombo(multiplier: Int, soundEnabled: Boolean, hapticEnabled: Boolean) {
        if (soundEnabled) {
            scope.launch {
                try {
                    val duration = 60
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, duration)
                    delay(70)
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, duration)
                    delay(70)
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, duration + 40)
                } catch (_: Exception) {}
            }
        }
        vibrate(hapticEnabled, (40 + multiplier * 15).coerceAtMost(100).toLong())
    }

    fun playLevelWin(soundEnabled: Boolean, hapticEnabled: Boolean) {
        if (soundEnabled) {
            scope.launch {
                try {
                    val tones = listOf(
                        ToneGenerator.TONE_PROP_BEEP,
                        ToneGenerator.TONE_PROP_BEEP2,
                        ToneGenerator.TONE_PROP_ACK,
                        ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD
                    )
                    for (tone in tones) {
                        toneGenerator?.startTone(tone, 80)
                        delay(90)
                    }
                } catch (_: Exception) {}
            }
        }
        vibrate(hapticEnabled, 80)
    }

    fun playGameOver(soundEnabled: Boolean, hapticEnabled: Boolean) {
        if (soundEnabled) {
            scope.launch {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 180)
                } catch (_: Exception) {}
            }
        }
        vibrate(hapticEnabled, 60)
    }

    private fun vibrate(enabled: Boolean, millis: Long) {
        if (!enabled || vibrator == null || !vibrator.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(millis)
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {}
    }
}
