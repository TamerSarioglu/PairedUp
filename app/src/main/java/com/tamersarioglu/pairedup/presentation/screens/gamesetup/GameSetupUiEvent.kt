package com.tamersarioglu.pairedup.presentation.screens.gamesetup

import com.tamersarioglu.pairedup.domain.model.GameDifficulty

sealed class GameSetupUiEvent {
    data class UpdatePlayerName(val name: String) : GameSetupUiEvent()
    data class SelectDifficulty(val difficulty: GameDifficulty) : GameSetupUiEvent()
    data object StartGame : GameSetupUiEvent()
    data object ClearError : GameSetupUiEvent()
}