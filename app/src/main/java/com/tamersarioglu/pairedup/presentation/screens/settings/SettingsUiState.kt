package com.tamersarioglu.pairedup.presentation.screens.settings

import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.model.SettingState

data class SettingsUiState(
    val settings: GameSettings = GameSettings(),
    val isLoading: Boolean = false,
    val showResetDialog: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    // Individual setting states for per-setting feedback
    val darkThemeState: SettingState = SettingState(),
    val timerState: SettingState = SettingState(),
    val soundState: SettingState = SettingState(),
    val vibrationState: SettingState = SettingState()
)