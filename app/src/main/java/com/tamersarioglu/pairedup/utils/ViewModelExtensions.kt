package com.tamersarioglu.pairedup.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun ViewModel.safeLaunch(
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
) {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    viewModelScope.launch(exceptionHandler) {
        block()
    }
}

fun ViewModel.debouncedAction(
    delayMs: Long = 500L,
    action: suspend () -> Unit
) {
    viewModelScope.launch {
        delay(delayMs)
        action()
    }
}

suspend fun <T> retryWithDelay(
    maxRetries: Int = 3,
    delayMs: Long = 1000L,
    action: suspend () -> T
): T {
    var lastException: Exception? = null

    repeat(maxRetries) { attempt ->
        try {
            return action()
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxRetries - 1) {
                delay(delayMs * (attempt + 1))
            }
        }
    }

    throw lastException ?: RuntimeException("Unknown error after $maxRetries retries")
}