package com.tamersarioglu.pairedup.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tamersarioglu.pairedup.domain.model.Language
import com.tamersarioglu.pairedup.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.SETTINGS_PREFERENCES
)

@Singleton
class LanguagePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey(Constants.LANGUAGE_KEY)
    }

    suspend fun saveLanguage(language: Language) {
        context.languageDataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.code
        }
    }

    fun getLanguage(): Flow<Language> = context.languageDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val languageCode = preferences[PreferencesKeys.LANGUAGE] ?: Language.ENGLISH.code
            Language.fromCode(languageCode)
        }

    fun observeLanguage(): Flow<Language> = getLanguage()

    // Synchronous method for immediate access (used in LocaleManager)
    fun getLanguageSync(): Language {
        return runBlocking {
            try {
                var result = Language.ENGLISH
                getLanguage().catch { 
                    emit(Language.ENGLISH) 
                }.collect { result = it }
                result
            } catch (e: Exception) {
                Language.ENGLISH
            }
        }
    }
}