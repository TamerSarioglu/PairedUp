package com.tamersarioglu.pairedup.presentation.screens.home

sealed class HomeUiEvent {
    data object NavigateToGameSetup : HomeUiEvent()
    data object NavigateToScores : HomeUiEvent()
    data object NavigateToSettings : HomeUiEvent()
    data object Refresh : HomeUiEvent()
    data object ClearError : HomeUiEvent()
}