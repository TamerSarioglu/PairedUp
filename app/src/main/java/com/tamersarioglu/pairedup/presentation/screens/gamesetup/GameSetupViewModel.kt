package com.tamersarioglu.pairedup.presentation.screens.gamesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getSettingsUseCase.getGameSettings()
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Ayarlar yüklenirken hata: ${throwable.message}"
                            )
                        }
                    }
                    .collect { settings ->
                        _uiState.update {
                            it.copy(
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

    fun updatePlayerName(name: String) {
        val trimmedName = name.trim()
        val isValid = trimmedName.length >= 2 && trimmedName.all { it.isLetter() || it.isWhitespace() }

        _uiState.update {
            it.copy(
                playerName = name,
                isPlayerNameValid = isValid,
                error = if (!isValid && name.isNotEmpty()) {
                    "Oyuncu adı en az 2 karakter olmalı ve sadece harf içermeli"
                } else null
            )
        }
    }

    fun selectDifficulty(difficulty: GameDifficulty) {
        _uiState.update {
            it.copy(selectedDifficulty = difficulty)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun validateAndPrepareGame(): Boolean {
        val currentState = _uiState.value

        return if (currentState.canStartGame) {
            _uiState.update { it.copy(isLoading = true) }
            true
        } else {
            _uiState.update {
                it.copy(
                    error = when {
                        currentState.playerName.isBlank() -> "Oyuncu adını giriniz"
                        !currentState.isPlayerNameValid -> "Geçerli bir oyuncu adı giriniz"
                        else -> "Oyunu başlatmak için tüm alanları doldurunuz"
                    }
                )
            }
            false
        }
    }
}