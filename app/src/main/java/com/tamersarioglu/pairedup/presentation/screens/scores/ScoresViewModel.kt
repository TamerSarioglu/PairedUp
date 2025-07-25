package com.tamersarioglu.pairedup.presentation.screens.scores


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.domain.usecase.GetScoresUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(
    private val getScoresUseCase: GetScoresUseCase,
    private val saveScoreUseCase: SaveScoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScoresUiState())
    val uiState: StateFlow<ScoresUiState> = _uiState.asStateFlow()

    init {
        loadScores()
    }

    private fun loadScores() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                getScoresUseCase.getAllScores()
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Skorlar yüklenirken hata: ${throwable.message}"
                            )
                        }
                    }
                    .collect { scores ->
                        _uiState.update {
                            it.copy(
                                scores = scores,
                                filteredScores = filterScoresByDifficulty(
                                    scores,
                                    it.selectedDifficulty
                                ),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Beklenmeyen hata: ${e.message}"
                    )
                }
            }
        }
    }

    fun filterByDifficulty(difficulty: GameDifficulty?) {
        val currentState = _uiState.value
        val filteredScores = filterScoresByDifficulty(currentState.scores, difficulty)

        _uiState.update {
            it.copy(
                selectedDifficulty = difficulty,
                filteredScores = filteredScores
            )
        }
    }

    private fun filterScoresByDifficulty(scores: List<Score>, difficulty: GameDifficulty?) =
        if (difficulty != null) {
            scores.filter { it.difficulty == difficulty }
        } else {
            scores
        }

    fun deleteScore(scoreId: Long) {
        viewModelScope.launch {
            try {
                saveScoreUseCase.deleteScoreById(scoreId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Skor silinirken hata: ${e.message}")
                }
            }
        }
    }

    fun showDeleteAllDialog() {
        _uiState.update { it.copy(showDeleteAllDialog = true) }
    }

    fun hideDeleteAllDialog() {
        _uiState.update { it.copy(showDeleteAllDialog = false) }
    }

    fun deleteAllScores() {
        viewModelScope.launch {
            try {
                saveScoreUseCase.deleteAllScores()
                _uiState.update { it.copy(showDeleteAllDialog = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Tüm skorlar silinirken hata: ${e.message}",
                        showDeleteAllDialog = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadScores()
    }
}