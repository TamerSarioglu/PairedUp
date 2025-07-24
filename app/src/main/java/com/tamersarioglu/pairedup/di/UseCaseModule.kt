package com.tamersarioglu.pairedup.di

import com.tamersarioglu.pairedup.domain.repository.ScoreRepository
import com.tamersarioglu.pairedup.domain.repository.SettingsRepository
import com.tamersarioglu.pairedup.domain.usecase.CalculateScoreUseCase
import com.tamersarioglu.pairedup.domain.usecase.GenerateCardsUseCase
import com.tamersarioglu.pairedup.domain.usecase.GetScoresUseCase
import com.tamersarioglu.pairedup.domain.usecase.GetSettingsUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveScoreUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetScoresUseCase(
        scoreRepository: ScoreRepository
    ): GetScoresUseCase {
        return GetScoresUseCase(scoreRepository)
    }

    @Provides
    @Singleton
    fun provideSaveScoreUseCase(
        scoreRepository: ScoreRepository
    ): SaveScoreUseCase {
        return SaveScoreUseCase(scoreRepository)
    }

    @Provides
    @Singleton
    fun provideGetSettingsUseCase(
        settingsRepository: SettingsRepository
    ): GetSettingsUseCase {
        return GetSettingsUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideSaveSettingsUseCase(
        settingsRepository: SettingsRepository
    ): SaveSettingsUseCase {
        return SaveSettingsUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideGenerateCardsUseCase(): GenerateCardsUseCase {
        return GenerateCardsUseCase()
    }

    @Provides
    @Singleton
    fun provideCalculateScoreUseCase(): CalculateScoreUseCase {
        return CalculateScoreUseCase()
    }
}