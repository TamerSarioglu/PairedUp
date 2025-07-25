package com.tamersarioglu.pairedup.di

import com.tamersarioglu.pairedup.data.repository.LocaleManagerImpl
import com.tamersarioglu.pairedup.domain.repository.LocaleManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleModule {

    @Binds
    @Singleton
    abstract fun bindLocaleManager(
        localeManagerImpl: LocaleManagerImpl
    ): LocaleManager
}