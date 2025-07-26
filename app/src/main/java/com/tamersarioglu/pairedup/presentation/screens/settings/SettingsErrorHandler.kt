package com.tamersarioglu.pairedup.presentation.screens.settings

import com.tamersarioglu.pairedup.R
import com.tamersarioglu.pairedup.data.provider.ResourceProvider
import com.tamersarioglu.pairedup.domain.model.ErrorType
import javax.inject.Inject

data class ErrorInfo(
    val message: String,
    val type: ErrorType
)

class SettingsErrorHandler @Inject constructor(
    private val resourceProvider: ResourceProvider
) {
    fun getErrorInfo(exception: Exception): ErrorInfo {
        return when (exception) {
            is java.io.IOException -> ErrorInfo(
                message = resourceProvider.getString(R.string.error_network_connection),
                type = ErrorType.NETWORK
            )

            is java.util.concurrent.TimeoutException -> ErrorInfo(
                message = resourceProvider.getString(R.string.error_operation_timeout),
                type = ErrorType.TIMEOUT
            )

            is SecurityException -> ErrorInfo(
                message = resourceProvider.getString(R.string.error_permission_denied),
                type = ErrorType.PERMISSION
            )

            is IllegalArgumentException -> ErrorInfo(
                message = resourceProvider.getString(
                    R.string.error_saving_setting,
                    "Invalid value"
                ),
                type = ErrorType.VALIDATION
            )

            is android.database.sqlite.SQLiteException -> ErrorInfo(
                message = resourceProvider.getString(
                    R.string.error_saving_setting,
                    "Storage error"
                ),
                type = ErrorType.STORAGE
            )

            else -> ErrorInfo(
                message = resourceProvider.getString(
                    R.string.error_saving_setting,
                    exception.message ?: "Unknown error"
                ),
                type = ErrorType.UNKNOWN
            )
        }
    }
}