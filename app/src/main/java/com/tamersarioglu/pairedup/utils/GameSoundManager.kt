package com.tamersarioglu.pairedup.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameSoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var soundPool: SoundPool? = null
    private var cardFlipSoundId: Int = 0
    private var matchSoundId: Int = 0
    private var gameWonSoundId: Int = 0
    private var gameLostSoundId: Int = 0

    private var isInitialized = false

    fun initialize() {
        if (isInitialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.let { pool ->
            // cardFlipSoundId = pool.load(context, R.raw.card_flip, 1)
            // matchSoundId = pool.load(context, R.raw.match, 1)
            // gameWonSoundId = pool.load(context, R.raw.game_won, 1)
            // gameLostSoundId = pool.load(context, R.raw.game_lost, 1)
        }

        isInitialized = true
    }

    fun playCardFlip() {
        soundPool?.play(cardFlipSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
    }

    fun playMatch() {
        soundPool?.play(matchSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playGameWon() {
        soundPool?.play(gameWonSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playGameLost() {
        soundPool?.play(gameLostSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isInitialized = false
    }
}