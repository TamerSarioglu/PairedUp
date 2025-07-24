package com.tamersarioglu.pairedup.domain.model

data class Score(
    val id: Int = 0,
    val playerName: String,
    val score: Int,
    val difficulty: GameDifficulty,
    val timeElapsed: Int,
    val attempts: Int,
    val timestamp: Long = System.currentTimeMillis()
)