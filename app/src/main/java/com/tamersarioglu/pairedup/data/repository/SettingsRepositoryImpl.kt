package com.tamersarioglu.pairedup.data.repository

import com.tamersarioglu.pairedup.data.local.datastore.SettingsDataStore
import com.tamersarioglu.pairedup.data.mapper.SettingsMapper
import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {

    override fun getGameSettings(): Flow<GameSettings> {
        return combine(
            settingsDataStore.isDarkTheme,
            settingsDataStore.isTimerEnabled,
            settingsDataStore.isSoundEnabled,
            settingsDataStore.isVibrationEnabled,
            settingsDataStore.gameTimeLimit
        ) { isDarkTheme, isTimerEnabled, isSoundEnabled, isVibrationEnabled, gameTimeLimit ->
            SettingsMapper.toDomain(
                isDarkTheme = isDarkTheme,
                isTimerEnabled = isTimerEnabled,
                isSoundEnabled = isSoundEnabled,
                isVibrationEnabled = isVibrationEnabled,
                gameTimeLimit = gameTimeLimit
            )
        }
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return settingsDataStore.isDarkTheme
    }

    override fun isTimerEnabled(): Flow<Boolean> {
        return settingsDataStore.isTimerEnabled
    }

    override fun isSoundEnabled(): Flow<Boolean> {
        return settingsDataStore.isSoundEnabled
    }

    override fun isVibrationEnabled(): Flow<Boolean> {
        return settingsDataStore.isVibrationEnabled
    }

    override fun getGameTimeLimit(): Flow<Int> {
        return settingsDataStore.gameTimeLimit
    }

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        settingsDataStore.setDarkTheme(isDarkTheme)
    }

    override suspend fun setTimerEnabled(isEnabled: Boolean) {
        settingsDataStore.setTimerEnabled(isEnabled)
    }

    override suspend fun setSoundEnabled(isEnabled: Boolean) {
        settingsDataStore.setSoundEnabled(isEnabled)
    }

    override suspend fun setVibrationEnabled(isEnabled: Boolean) {
        settingsDataStore.setVibrationEnabled(isEnabled)
    }

    override suspend fun setGameTimeLimit(timeLimit: Int) {
        settingsDataStore.setGameTimeLimit(timeLimit)
    }

    override suspend fun resetAllSettings() {
        settingsDataStore.resetAllSettings()
    }
}