package com.tamersarioglu.pairedup.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.R
import com.tamersarioglu.pairedup.data.provider.ResourceProvider
import com.tamersarioglu.pairedup.domain.usecase.GetSettingsUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveScoreUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val saveScoreUseCase: SaveScoreUseCase,
    private val resourceProvider: ResourceProvider
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
                                error = resourceProvider.getString(R.string.error_loading_settings, throwable.message ?: "")
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
                        error = resourceProvider.getString(R.string.error_unexpected, e.message ?: "")
                    )
                }
            }
        }
    }

    fun updateDarkTheme(isDarkTheme: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setDarkTheme(isDarkTheme) },
            successMessage = resourceProvider.getString(R.string.success_theme_changed)
        )
    }

    fun updateTimerEnabled(isEnabled: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setTimerEnabled(isEnabled) },
            successMessage = if (isEnabled) {
                resourceProvider.getString(R.string.success_timer_enabled)
            } else {
                resourceProvider.getString(R.string.success_timer_disabled)
            }
        )
    }

    fun updateSoundEnabled(isEnabled: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setSoundEnabled(isEnabled) },
            successMessage = if (isEnabled) {
                resourceProvider.getString(R.string.success_sound_enabled)
            } else {
                resourceProvider.getString(R.string.success_sound_disabled)
            }
        )
    }

    fun updateVibrationEnabled(isEnabled: Boolean) {
        updateSetting(
            updateAction = { saveSettingsUseCase.setVibrationEnabled(isEnabled) },
            successMessage = if (isEnabled) {
                resourceProvider.getString(R.string.success_vibration_enabled)
            } else {
                resourceProvider.getString(R.string.success_vibration_disabled)
            }
        )
    }

    fun updateGameTimeLimit(timeLimit: Int) {
        if (timeLimit in 30..300) {
            updateSetting(
                updateAction = { saveSettingsUseCase.setGameTimeLimit(timeLimit) },
                successMessage = resourceProvider.getString(R.string.success_game_time_set, timeLimit)
            )
        } else {
            _uiState.update {
                it.copy(error = resourceProvider.getString(R.string.error_game_time_limit))
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

                delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = resourceProvider.getString(R.string.error_saving_setting, e.message ?: "")
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
                        successMessage = resourceProvider.getString(R.string.success_all_settings_reset)
                    )
                }

                delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = resourceProvider.getString(R.string.error_resetting_settings, e.message ?: "")
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
                        successMessage = resourceProvider.getString(R.string.success_all_scores_cleared)
                    )
                }

                delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = resourceProvider.getString(R.string.error_clearing_scores, e.message ?: "")
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