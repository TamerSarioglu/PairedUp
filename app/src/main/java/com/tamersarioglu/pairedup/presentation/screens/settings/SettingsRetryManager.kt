package com.tamersarioglu.pairedup.presentation.screens.settings

import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.model.SettingState
import com.tamersarioglu.pairedup.domain.model.SettingType
import com.tamersarioglu.pairedup.domain.usecase.SaveSettingsUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class SettingsRetryManager @Inject constructor(
    private val saveSettingsUseCase: SaveSettingsUseCase
) {
    fun calculateBackoffDelay(retryCount: Int): Long {
        val baseDelay = 1000L
        val maxDelay = 8000L
        val exponentialDelay = baseDelay * (1L shl retryCount)
        return minOf(exponentialDelay, maxDelay)
    }

    fun getSettingState(uiState: SettingsUiState, settingType: SettingType): SettingState {
        return when (settingType) {
            SettingType.DARK_THEME -> uiState.darkThemeState
            SettingType.TIMER_ENABLED -> uiState.timerState
            SettingType.SOUND_ENABLED -> uiState.soundState
            SettingType.VIBRATION_ENABLED -> uiState.vibrationState
        }
    }

    fun createRetryAction(
        settingType: SettingType,
        currentSettings: GameSettings
    ): Pair<(GameSettings) -> GameSettings, suspend () -> Unit> {
        return when (settingType) {
            SettingType.DARK_THEME -> {
                val value = currentSettings.isDarkTheme
                { settings: GameSettings -> settings.copy(isDarkTheme = value) } to 
                suspend { saveSettingsUseCase.setDarkTheme(value) }
            }
            SettingType.TIMER_ENABLED -> {
                val value = currentSettings.isTimerEnabled
                { settings: GameSettings -> settings.copy(isTimerEnabled = value) } to 
                suspend { saveSettingsUseCase.setTimerEnabled(value) }
            }
            SettingType.SOUND_ENABLED -> {
                val value = currentSettings.isSoundEnabled
                { settings: GameSettings -> settings.copy(isSoundEnabled = value) } to 
                suspend { saveSettingsUseCase.setSoundEnabled(value) }
            }
            SettingType.VIBRATION_ENABLED -> {
                val value = currentSettings.isVibrationEnabled
                { settings: GameSettings -> settings.copy(isVibrationEnabled = value) } to 
                suspend { saveSettingsUseCase.setVibrationEnabled(value) }
            }
        }
    }
}