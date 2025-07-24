package com.tamersarioglu.pairedup.presentation.screens.scores

import com.tamersarioglu.pairedup.domain.model.GameDifficulty

sealed class ScoresUiEvent {
    data class FilterByDifficulty(val difficulty: GameDifficulty?) : ScoresUiEvent()
    data class DeleteScore(val scoreId: Long) : ScoresUiEvent()
    data object ShowDeleteAllDialog : ScoresUiEvent()
    data object HideDeleteAllDialog : ScoresUiEvent()
    data object DeleteAllScores : ScoresUiEvent()
    data object Refresh : ScoresUiEvent()
    data object ClearError : ScoresUiEvent()
}