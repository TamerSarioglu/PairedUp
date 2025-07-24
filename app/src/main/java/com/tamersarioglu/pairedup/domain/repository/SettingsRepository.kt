package com.tamersarioglu.pairedup.domain.repository

import com.tamersarioglu.pairedup.domain.model.GameSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getGameSettings(): Flow<GameSettings>

    fun isDarkTheme(): Flow<Boolean>

    fun isTimerEnabled(): Flow<Boolean>

    fun isSoundEnabled(): Flow<Boolean>

    fun isVibrationEnabled(): Flow<Boolean>

    fun getGameTimeLimit(): Flow<Int>

    suspend fun setDarkTheme(isDarkTheme: Boolean)

    suspend fun setTimerEnabled(isEnabled: Boolean)

    suspend fun setSoundEnabled(isEnabled: Boolean)

    suspend fun setVibrationEnabled(isEnabled: Boolean)

    suspend fun setGameTimeLimit(timeLimit: Int)

    suspend fun resetAllSettings()
}