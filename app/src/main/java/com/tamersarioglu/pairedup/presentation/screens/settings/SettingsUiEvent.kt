package com.tamersarioglu.pairedup.presentation.screens.settings

sealed class SettingsUiEvent {
    data class UpdateDarkTheme(val isDarkTheme: Boolean) : SettingsUiEvent()
    data class UpdateTimerEnabled(val isEnabled: Boolean) : SettingsUiEvent()
    data class UpdateSoundEnabled(val isEnabled: Boolean) : SettingsUiEvent()
    data class UpdateVibrationEnabled(val isEnabled: Boolean) : SettingsUiEvent()
    data class UpdateGameTimeLimit(val timeLimit: Int) : SettingsUiEvent()
    data object ShowResetDialog : SettingsUiEvent()
    data object HideResetDialog : SettingsUiEvent()
    data object ResetAllSettings : SettingsUiEvent()
    data object ClearScores : SettingsUiEvent()
    data object ClearError : SettingsUiEvent()
    data object ClearSuccessMessage : SettingsUiEvent()
}