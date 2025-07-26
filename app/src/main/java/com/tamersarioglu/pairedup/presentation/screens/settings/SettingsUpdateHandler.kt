package com.tamersarioglu.pairedup.presentation.screens.settings

import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.model.SettingState
import com.tamersarioglu.pairedup.domain.model.SettingType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SettingsUpdateHandler @Inject constructor(
    private val errorHandler: SettingsErrorHandler,
    private val stateManager: SettingsStateManager
) {
    suspend fun performSettingUpdateWithRetry(
        uiState: MutableStateFlow<SettingsUiState>,
        settingType: SettingType,
        optimisticUpdate: (GameSettings) -> GameSettings,
        updateAction: suspend () -> Unit,
        retryCount: Int = 0
    ) {
        val currentSettingValue = stateManager.getCurrentSettingValue(uiState.value.settings, settingType)
        
        uiState.value = uiState.value.copy(
            settings = optimisticUpdate(uiState.value.settings)
        ).let { state ->
            stateManager.run {
                state.updateSettingState(settingType) { 
                    it.copy(
                        isLoading = true, 
                        hasError = false, 
                        showSuccess = false,
                        retryCount = retryCount
                    ) 
                }
            }
        }

        try {
            updateAction()

            uiState.value = stateManager.run {
                uiState.value.updateSettingState(settingType) { 
                    it.copy(
                        isLoading = false, 
                        hasError = false, 
                        showSuccess = true,
                        retryCount = 0,
                        errorMessage = null
                    ) 
                }
            }

            delay(1000)
            uiState.value = stateManager.run {
                uiState.value.updateSettingState(settingType) { 
                    it.copy(showSuccess = false) 
                }
            }

        } catch (e: Exception) {
            val errorInfo = errorHandler.getErrorInfo(e)
            val newRetryCount = retryCount + 1
            
            uiState.value = uiState.value.copy(
                settings = stateManager.rollbackSetting(uiState.value.settings, settingType, currentSettingValue)
            ).let { state ->
                stateManager.run {
                    state.updateSettingState(settingType) { 
                        it.copy(
                            isLoading = false, 
                            hasError = true, 
                            showSuccess = false,
                            retryCount = newRetryCount,
                            errorMessage = errorInfo.message,
                            errorType = errorInfo.type,
                            canRetry = errorInfo.type.isRetryable && newRetryCount < SettingState.MAX_RETRY_ATTEMPTS
                        ) 
                    }
                }
            }
        }
    }
}