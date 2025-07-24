package com.tamersarioglu.pairedup.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tamersarioglu.pairedup.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.SETTINGS_PREFERENCES
)

class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKeys{
        val DARK_THEME = booleanPreferencesKey(Constants.DARK_THEME_KEY)
        val TIMER_ENABLED = booleanPreferencesKey(Constants.TIMER_ENABLED_KEY)
        val SOUND_ENABLED = booleanPreferencesKey(Constants.SOUND_ENABLED_KEY)
        val VIBRATION_ENABLED = booleanPreferencesKey(Constants.VIBRATION_ENABLED_KEY)
        val GAME_TIME_LIMIT = intPreferencesKey(Constants.GAME_TIME_LIMIT_KEY)
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.DARK_THEME] ?: false
        }

    val isTimerEnabled: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.TIMER_ENABLED] ?: true
        }

    val isSoundEnabled: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] ?: true
        }

    val isVibrationEnabled: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.VIBRATION_ENABLED] ?: true
        }

    val gameTimeLimit: Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.GAME_TIME_LIMIT] ?: Constants.GAME_TIME_LIMIT
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = isDarkTheme
        }
    }

    suspend fun setTimerEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_ENABLED] = isEnabled
        }
    }

    suspend fun setSoundEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = isEnabled
        }
    }

    suspend fun setVibrationEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.VIBRATION_ENABLED] = isEnabled
        }
    }

    suspend fun setGameTimeLimit(timeLimit: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.GAME_TIME_LIMIT] = timeLimit
        }
    }

    suspend fun resetAllSettings() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

