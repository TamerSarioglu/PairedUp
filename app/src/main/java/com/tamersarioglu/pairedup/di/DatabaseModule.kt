package com.tamersarioglu.pairedup.di

import android.content.Context
import androidx.compose.ui.unit.Constraints
import androidx.room.Room
import com.tamersarioglu.pairedup.data.local.database.MemoryGameDatabase
import com.tamersarioglu.pairedup.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMemoryGameDatabase(
        @ApplicationContext context: Context
    ): MemoryGameDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MemoryGameDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration(false)
            .build()
    }



}