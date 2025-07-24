package com.tamersarioglu.pairedup.domain.model


data class GameState(
    val cards: List<Card> = emptyList(),
    val score: Int = 0,
    val timeLeft: Int = 60,
    val gameStatus: GameStatus = GameStatus.SETUP,
    val flippedCards: List<Card> = emptyList(),
    val matchedPairs: Int = 0,
    val attempts: Int = 0,
    val difficulty: GameDifficulty = GameDifficulty.EASY,
    val playerName: String = "",
    val isTimerEnabled: Boolean = true
)

enum class GameStatus {
    SETUP,
    PLAYING,
    PAUSED,
    WON,
    LOST
}