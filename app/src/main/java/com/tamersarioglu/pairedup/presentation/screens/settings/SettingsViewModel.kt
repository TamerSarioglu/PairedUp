package com.tamersarioglu.pairedup.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.R
import com.tamersarioglu.pairedup.data.provider.ResourceProvider
import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.model.SettingType
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
    private val resourceProvider: ResourceProvider,
    private val updateHandler: SettingsUpdateHandler,
    private val retryManager: SettingsRetryManager,
    private val stateManager: SettingsStateManager
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
        updateSettingWithState(
            settingType = SettingType.DARK_THEME,
            optimisticUpdate = { settings -> settings.copy(isDarkTheme = isDarkTheme) },
            updateAction = { saveSettingsUseCase.setDarkTheme(isDarkTheme) }
        )
    }

    fun updateTimerEnabled(isEnabled: Boolean) {
        updateSettingWithState(
            settingType = SettingType.TIMER_ENABLED,
            optimisticUpdate = { settings -> settings.copy(isTimerEnabled = isEnabled) },
            updateAction = { saveSettingsUseCase.setTimerEnabled(isEnabled) }
        )
    }

    fun updateSoundEnabled(isEnabled: Boolean) {
        updateSettingWithState(
            settingType = SettingType.SOUND_ENABLED,
            optimisticUpdate = { settings -> settings.copy(isSoundEnabled = isEnabled) },
            updateAction = { saveSettingsUseCase.setSoundEnabled(isEnabled) }
        )
    }

    fun updateVibrationEnabled(isEnabled: Boolean) {
        updateSettingWithState(
            settingType = SettingType.VIBRATION_ENABLED,
            optimisticUpdate = { settings -> settings.copy(isVibrationEnabled = isEnabled) },
            updateAction = { saveSettingsUseCase.setVibrationEnabled(isEnabled) }
        )
    }

    fun updateGameTimeLimit(timeLimit: Int) {
        if (timeLimit in 30..300) {
            viewModelScope.launch {
                _uiState.update { it.copy(error = null, successMessage = null) }

                try {
                    saveSettingsUseCase.setGameTimeLimit(timeLimit)
                    
                    _uiState.update {
                        it.copy(
                            settings = it.settings.copy(gameTimeLimit = timeLimit),
                            successMessage = resourceProvider.getString(R.string.success_game_time_set, timeLimit),
                            error = null
                        )
                    }

                    delay(3000)
                    _uiState.update { it.copy(successMessage = null) }

                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            error = resourceProvider.getString(R.string.error_saving_setting, e.message ?: "")
                        )
                    }
                }
            }
        } else {
            _uiState.update {
                it.copy(error = resourceProvider.getString(R.string.error_game_time_limit))
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
                    showResetDialog = false,
                    error = null,
                    successMessage = null
                )
            }

            try {
                saveSettingsUseCase.resetAllSettings()

                _uiState.update {
                    it.copy(
                        successMessage = resourceProvider.getString(R.string.success_all_settings_reset)
                    )
                }

                delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = resourceProvider.getString(R.string.error_resetting_settings, e.message ?: "")
                    )
                }
            }
        }
    }

    fun clearScores() {
        viewModelScope.launch {
            _uiState.update { it.copy(error = null, successMessage = null) }

            try {
                saveScoreUseCase.deleteAllScores()

                _uiState.update {
                    it.copy(
                        successMessage = resourceProvider.getString(R.string.success_all_scores_cleared)
                    )
                }

                delay(3000)
                _uiState.update { it.copy(successMessage = null) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
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

    private fun updateSettingWithState(
        settingType: SettingType,
        optimisticUpdate: (GameSettings) -> GameSettings,
        updateAction: suspend () -> Unit
    ) {
        viewModelScope.launch {
            updateHandler.performSettingUpdateWithRetry(
                uiState = _uiState,
                settingType = settingType,
                optimisticUpdate = optimisticUpdate,
                updateAction = updateAction,
                retryCount = 0
            )
        }
    }

    fun retrySetting(settingType: SettingType) {
        val currentState = _uiState.value
        val settingState = retryManager.getSettingState(currentState, settingType)
        
        if (!settingState.isRetryAvailable) return

        viewModelScope.launch {
            val backoffDelay = retryManager.calculateBackoffDelay(settingState.retryCount)
            delay(backoffDelay)

            val (optimisticUpdate, updateAction) = retryManager.createRetryAction(
                settingType, 
                currentState.settings
            )

            updateHandler.performSettingUpdateWithRetry(
                uiState = _uiState,
                settingType = settingType,
                optimisticUpdate = optimisticUpdate,
                updateAction = updateAction,
                retryCount = settingState.retryCount
            )
        }
    }

    fun clearSettingError(settingType: SettingType) {
        _uiState.update { currentState ->
            stateManager.clearSettingError(currentState, settingType)
        }
    }
}