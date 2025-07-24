package com.tamersarioglu.pairedup.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tamersarioglu.pairedup.utils.Constants

@Entity(tableName = Constants.SCORE_TABLE_NAME)
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "player_name")
    val playerName: String,

    @ColumnInfo(name = "score")
    val score: Int,

    @ColumnInfo(name = "difficulty")
    val difficulty: String,

    @ColumnInfo(name = "time_elapsed")
    val timeElapsed: Int, // in seconds

    @ColumnInfo(name = "attempts")
    val attempts: Int,

    @ColumnInfo(name = "timestamp")
val timestamp: Long = System.currentTimeMillis()
)