package com.tamersarioglu.pairedup.domain.repository

import android.content.Context
import com.tamersarioglu.pairedup.domain.model.Language
import kotlinx.coroutines.flow.Flow

interface LocaleManager {
    suspend fun setLanguage(language: Language)
    suspend fun getCurrentLanguage(): Language
    fun getLocalizedContext(context: Context): Context
    fun observeLanguageChanges(): Flow<Language>
}