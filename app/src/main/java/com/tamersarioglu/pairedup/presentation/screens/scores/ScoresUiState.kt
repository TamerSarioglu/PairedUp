package com.tamersarioglu.pairedup.presentation.screens.scores

import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score

data class ScoresUiState(
    val scores: List<Score> = emptyList(),
    val filteredScores: List<Score> = emptyList(),
    val selectedDifficulty: GameDifficulty? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteAllDialog: Boolean = false
) {
    val isEmpty: Boolean
        get() = scores.isEmpty()

    val displayScores: List<Score>
        get() = if (selectedDifficulty != null) {
            filteredScores
        } else {
            scores
        }
}