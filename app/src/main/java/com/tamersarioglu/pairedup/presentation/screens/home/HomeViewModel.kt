package com.tamersarioglu.pairedup.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.domain.usecase.GetScoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getScoresUseCase: GetScoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                getScoresUseCase.getAllScores()
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Skorlar yüklenirken hata oluştu: ${throwable.message}"
                            )
                        }
                    }
                    .collect { scores ->
                        val hasScores = scores.isNotEmpty()
                        val recentHighScore = scores.maxByOrNull { it.score }?.score ?: 0

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                hasScores = hasScores,
                                recentHighScore = recentHighScore,
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

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadHomeData()
    }
}