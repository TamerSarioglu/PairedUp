package com.tamersarioglu.pairedup.di

import com.tamersarioglu.pairedup.data.provider.ResourceProvider
import com.tamersarioglu.pairedup.data.provider.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {
    
    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        resourceProviderImpl: ResourceProviderImpl
    ): ResourceProvider
}