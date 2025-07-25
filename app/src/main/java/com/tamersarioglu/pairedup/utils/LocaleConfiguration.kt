package com.tamersarioglu.pairedup.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.tamersarioglu.pairedup.domain.model.Language
import java.util.Locale

object LocaleConfiguration {
    
    fun createLocalizedContext(context: Context, language: Language): Context {
        val locale = when (language) {
            Language.TURKISH -> Locale.Builder().setLanguage("tr").setRegion("TR").build()
            Language.ENGLISH -> Locale.Builder().setLanguage("en").setRegion("US").build()
        }

        return updateContextLocale(context, locale)
    }

    private fun updateContextLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
            context.createConfigurationContext(configuration)
        }
    }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }

    fun localeToLanguage(locale: Locale): Language {
        return when (locale.language) {
            "tr" -> Language.TURKISH
            else -> Language.ENGLISH
        }
    }
}