package com.tamersarioglu.pairedup.data.mapper

import com.tamersarioglu.pairedup.domain.model.GameSettings

object SettingsMapper {

    fun toDomain(
        isDarkTheme: Boolean,
        isTimerEnabled: Boolean,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean,
        gameTimeLimit: Int
    ): GameSettings {
        return GameSettings(
            isDarkTheme = isDarkTheme,
            isTimerEnabled = isTimerEnabled,
            isSoundEnabled = isSoundEnabled,
            isVibrationEnabled = isVibrationEnabled,
            gameTimeLimit = gameTimeLimit
        )
    }
}