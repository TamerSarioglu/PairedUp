package com.tamersarioglu.pairedup.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tamersarioglu.pairedup.data.local.database.dao.ScoreDao
import com.tamersarioglu.pairedup.data.local.database.entities.ScoreEntity
import com.tamersarioglu.pairedup.utils.Constants

@Database(
    entities = [ScoreEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MemoryGameDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: MemoryGameDatabase? = null

        fun getDatabase(context: Context): MemoryGameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoryGameDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}