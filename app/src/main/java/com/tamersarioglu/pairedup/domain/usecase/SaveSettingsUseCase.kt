package com.tamersarioglu.pairedup.domain.usecase

import com.tamersarioglu.pairedup.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        settingsRepository.setDarkTheme(isDarkTheme)
    }

    suspend fun setTimerEnabled(isEnabled: Boolean) {
        settingsRepository.setTimerEnabled(isEnabled)
    }

    suspend fun setSoundEnabled(isEnabled: Boolean) {
        settingsRepository.setSoundEnabled(isEnabled)
    }

    suspend fun setVibrationEnabled(isEnabled: Boolean) {
        settingsRepository.setVibrationEnabled(isEnabled)
    }

    suspend fun setGameTimeLimit(timeLimit: Int) {
        settingsRepository.setGameTimeLimit(timeLimit)
    }

    suspend fun resetAllSettings() {
        settingsRepository.resetAllSettings()
    }
}