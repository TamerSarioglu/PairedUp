package com.tamersarioglu.pairedup.domain.model

data class GameSettings(
    val isDarkTheme: Boolean = false,
    val isTimerEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true,
    val gameTimeLimit: Int = 60
)