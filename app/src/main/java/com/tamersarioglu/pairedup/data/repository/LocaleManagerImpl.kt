package com.tamersarioglu.pairedup.data.repository

import android.content.Context
import com.tamersarioglu.pairedup.data.local.datastore.LanguagePreferences
import com.tamersarioglu.pairedup.domain.model.Language
import com.tamersarioglu.pairedup.domain.repository.LocaleManager
import com.tamersarioglu.pairedup.utils.LocaleConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languagePreferences: LanguagePreferences
) : LocaleManager {

    override suspend fun setLanguage(language: Language) {
        languagePreferences.saveLanguage(language)
    }

    override suspend fun getCurrentLanguage(): Language {
        return languagePreferences.getLanguage().first()
    }

    override fun getLocalizedContext(context: Context): Context {
        return try {
            val language = languagePreferences.getLanguageSync()
            LocaleConfiguration.createLocalizedContext(context, language)
        } catch (e: Exception) {
            // Fallback to original context if locale configuration fails
            context
        }
    }

    override fun observeLanguageChanges(): Flow<Language> {
        return languagePreferences.observeLanguage()
    }
}