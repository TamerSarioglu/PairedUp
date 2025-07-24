package com.tamersarioglu.pairedup.presentation.screens.game

sealed class GameSideEffect {
    data object NavigateToHome : GameSideEffect()
    data object NavigateToScores : GameSideEffect()
    data object ShowScoreSavedMessage : GameSideEffect()
    data class ShowError(val message: String) : GameSideEffect()
    data object PlayCardFlipSound : GameSideEffect()
    data object PlayMatchSound : GameSideEffect()
    data object PlayGameWonSound : GameSideEffect()
    data object PlayGameLostSound : GameSideEffect()
    data object VibrateOnMatch : GameSideEffect()
    data object VibrateOnMismatch : GameSideEffect()
}
