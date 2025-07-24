package com.tamersarioglu.pairedup.presentation.screens.gamesetup

import com.tamersarioglu.pairedup.domain.model.GameDifficulty

data class GameSetupUiState(
    val playerName: String = "",
    val selectedDifficulty: GameDifficulty = GameDifficulty.EASY,
    val isPlayerNameValid: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val canStartGame: Boolean
        get() = isPlayerNameValid && !isLoading
}