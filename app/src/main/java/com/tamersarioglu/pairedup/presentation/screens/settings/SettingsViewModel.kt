package com.tamersarioglu.pairedup.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.domain.usecase.GetSettingsUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveScoreUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val saveScoreUseCase: SaveScoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

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
                                settings = settings,
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

    fun updateDarkTheme(isDarkTheme: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setDarkTheme(isDarkTheme) },
            successMessage = "Tema değiştirildi"
        )
    }

    fun updateTimerEnabled(isEnabled: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setTimerEnabled(isEnabled) },
            successMessage = if (isEnabled) "Zamanlayıcı açıldı" else "Zamanlayıcı kapatıldı"
        )
    }

    fun updateSoundEnabled(isEnabled: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setSoundEnabled(isEnabled) },
            successMessage = if (isEnabled) "Ses efektleri açıldı" else "Ses efektleri kapatıldı"
        )
    }

    fun updateVibrationEnabled(isEnabled: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setVibrationEnabled(isEnabled) },
            successMessage = if (isEnabled) "Titreşim açıldı" else "Titreşim kapatıldı"
        )
    }

    fun updateGameTimeLimit(timeLimit: Int) {
        if (timeLimit in 30..300) { // 30 seconds to 5 minutes
            updateSetting(
                updateAction = { saveSettingsUseCase.setGameTimeLimit(timeLimit) },
                successMessage = "Oyun süresi ${timeLimit} saniye olarak ayarlandı"
            )
        } else {
            _uiState.update {
                it.copy(error = "Oyun süresi 30-300 saniye arası olmalıdır")
            }
        }
    }

    private fun updateSetting(
        updateAction: suspend () -> Unit,
        successMessage: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }

            try {
                updateAction()

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = successMessage,
                        error = null
                    )
                }

                // Clear success message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Ayar kaydedilirken hata: ${e.message}"
                    )
                }
            }
        }
    }

    fun showResetDialog() {
        _uiState.update { it.copy(showResetDialog = true) }
    }

    fun hideResetDialog() {
        _uiState.update { it.copy(showResetDialog = false) }
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    showResetDialog = false,
                    error = null,
                    successMessage = null
                )
            }

            try {
                saveSettingsUseCase.resetAllSettings()

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = "Tüm ayarlar sıfırlandı"
                    )
                }

                // Clear success message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Ayarlar sıfırlanırken hata: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearScores() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }

            try {
                saveScoreUseCase.deleteAllScores()

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = "Tüm skorlar silindi"
                    )
                }

                // Clear success message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Skorlar silinirken hata: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}