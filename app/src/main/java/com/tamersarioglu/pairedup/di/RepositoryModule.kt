package com.tamersarioglu.pairedup.di

import com.tamersarioglu.pairedup.data.repository.ScoreRepositoryImpl
import com.tamersarioglu.pairedup.data.repository.SettingsRepositoryImpl
import com.tamersarioglu.pairedup.domain.repository.ScoreRepository
import com.tamersarioglu.pairedup.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScoreRepository(
        scoreRepositoryImpl: ScoreRepositoryImpl
    ): ScoreRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}