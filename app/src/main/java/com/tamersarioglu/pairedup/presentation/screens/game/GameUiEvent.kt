package com.tamersarioglu.pairedup.presentation.screens.game

import com.tamersarioglu.pairedup.domain.model.Card

sealed class GameUiEvent {
    data class CardClicked(val card: Card) : GameUiEvent()
    data object PauseGame : GameUiEvent()
    data object ResumeGame : GameUiEvent()
    data object SaveScore : GameUiEvent()
    data object PlayAgain : GameUiEvent()
    data object GoHome : GameUiEvent()
    data object DismissDialog : GameUiEvent()
    data object ClearError : GameUiEvent()
}
