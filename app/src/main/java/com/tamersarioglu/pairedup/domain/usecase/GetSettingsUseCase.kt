package com.tamersarioglu.pairedup.domain.usecase

import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun getGameSettings(): Flow<GameSettings> {
        return settingsRepository.getGameSettings()
    }

    fun isDarkTheme(): Flow<Boolean> {
        return settingsRepository.isDarkTheme()
    }

    fun isTimerEnabled(): Flow<Boolean> {
        return settingsRepository.isTimerEnabled()
    }

    fun isSoundEnabled(): Flow<Boolean> {
        return settingsRepository.isSoundEnabled()
    }

    fun isVibrationEnabled(): Flow<Boolean> {
        return settingsRepository.isVibrationEnabled()
    }

    fun getGameTimeLimit(): Flow<Int> {
        return settingsRepository.getGameTimeLimit()
    }
}