package com.tamersarioglu.pairedup.presentation.screens.settings

import com.tamersarioglu.pairedup.domain.model.GameSettings

data class SettingsUiState(
    val settings: GameSettings = GameSettings(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val showResetDialog: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)