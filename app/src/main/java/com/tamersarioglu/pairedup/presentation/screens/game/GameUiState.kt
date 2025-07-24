package com.tamersarioglu.pairedup.presentation.screens.game

import com.tamersarioglu.pairedup.domain.model.GameState
import com.tamersarioglu.pairedup.domain.model.GameStatus

data class GameUiState(
    val gameState: GameState = GameState(),
    val showResultDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val scoreSaved: Boolean = false
) {
    val canInteract: Boolean
        get() = gameState.gameStatus == GameStatus.PLAYING &&
                gameState.flippedCards.size < 2 &&
                !isLoading
}