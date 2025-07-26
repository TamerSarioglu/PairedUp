package com.tamersarioglu.pairedup.domain.model

data class SettingState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val showSuccess: Boolean = false,
    val retryCount: Int = 0,
    val errorMessage: String? = null,
    val canRetry: Boolean = true,
    val errorType: ErrorType = ErrorType.UNKNOWN
) {
    val isIdle: Boolean
        get() = !isLoading && !hasError && !showSuccess

    val hasActiveFeedback: Boolean
        get() = isLoading || hasError || showSuccess

    val hasReachedMaxRetries: Boolean
        get() = retryCount >= MAX_RETRY_ATTEMPTS

    val isRetryAvailable: Boolean
        get() = hasError && canRetry && !hasReachedMaxRetries

    companion object {
        const val MAX_RETRY_ATTEMPTS = 3
    }
}

enum class ErrorType {
    NETWORK,
    TIMEOUT,
    PERMISSION,
    VALIDATION,
    STORAGE,
    UNKNOWN;

    val isRetryable: Boolean
        get() = when (this) {
            NETWORK, TIMEOUT, STORAGE -> true
            PERMISSION, VALIDATION, UNKNOWN -> false
        }
}