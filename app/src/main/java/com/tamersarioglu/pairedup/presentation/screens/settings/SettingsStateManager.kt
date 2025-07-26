package com.tamersarioglu.pairedup.presentation.screens.settings

import com.tamersarioglu.pairedup.domain.model.ErrorType
import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.model.SettingState
import com.tamersarioglu.pairedup.domain.model.SettingType
import javax.inject.Inject

class SettingsStateManager @Inject constructor() {
    
    fun getCurrentSettingValue(settings: GameSettings, settingType: SettingType): Any {
        return when (settingType) {
            SettingType.DARK_THEME -> settings.isDarkTheme
            SettingType.TIMER_ENABLED -> settings.isTimerEnabled
            SettingType.SOUND_ENABLED -> settings.isSoundEnabled
            SettingType.VIBRATION_ENABLED -> settings.isVibrationEnabled
        }
    }

    fun rollbackSetting(settings: GameSettings, settingType: SettingType, previousValue: Any): GameSettings {
        return when (settingType) {
            SettingType.DARK_THEME -> settings.copy(isDarkTheme = previousValue as Boolean)
            SettingType.TIMER_ENABLED -> settings.copy(isTimerEnabled = previousValue as Boolean)
            SettingType.SOUND_ENABLED -> settings.copy(isSoundEnabled = previousValue as Boolean)
            SettingType.VIBRATION_ENABLED -> settings.copy(isVibrationEnabled = previousValue as Boolean)
        }
    }

    fun SettingsUiState.updateSettingState(
        settingType: SettingType,
        update: (SettingState) -> SettingState
    ): SettingsUiState {
        return when (settingType) {
            SettingType.DARK_THEME -> copy(darkThemeState = update(darkThemeState))
            SettingType.TIMER_ENABLED -> copy(timerState = update(timerState))
            SettingType.SOUND_ENABLED -> copy(soundState = update(soundState))
            SettingType.VIBRATION_ENABLED -> copy(vibrationState = update(vibrationState))
        }
    }

    fun clearSettingError(
        uiState: SettingsUiState,
        settingType: SettingType
    ): SettingsUiState {
        return uiState.updateSettingState(settingType) { 
            it.copy(
                hasError = false,
                errorMessage = null,
                retryCount = 0,
                errorType = ErrorType.UNKNOWN
            ) 
        }
    }
}