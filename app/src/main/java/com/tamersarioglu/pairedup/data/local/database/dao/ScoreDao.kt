package com.tamersarioglu.pairedup.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tamersarioglu.pairedup.data.local.database.entities.ScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Query("SELECT * FROM scores ORDER BY score DESC, timestamp DESC")
    fun getAllScores(): Flow<List<ScoreEntity>>

    @Query("SELECT * FROM scores WHERE difficulty = :difficulty ORDER BY score DESC, timestamp DESC")
    fun getScoresByDifficulty(difficulty: String): Flow<List<ScoreEntity>>

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT :limit")
    fun getTopScores(limit: Int): Flow<List<ScoreEntity>>

    @Query("SELECT * FROM scores WHERE player_name = :playerName ORDER BY score DESC, timestamp DESC")
    fun getScoresByPlayer(playerName: String): Flow<List<ScoreEntity>>

    @Query("SELECT MAX(score) FROM scores WHERE difficulty = :difficulty")
    suspend fun getHighestScoreByDifficulty(difficulty: String): Int?

    @Query("SELECT COUNT(*) FROM scores")
    suspend fun getScoreCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: ScoreEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScores(scores: List<ScoreEntity>)

    @Update
    suspend fun updateScore(score: ScoreEntity)

    @Delete
    suspend fun deleteScore(score: ScoreEntity)

    @Query("DELETE FROM scores")
    suspend fun deleteAllScores()

    @Query("DELETE FROM scores WHERE difficulty = :difficulty")
    suspend fun deleteScoresByDifficulty(difficulty: String)

    @Query("DELETE FROM scores WHERE id = :scoreId")
    suspend fun deleteScoreById(scoreId: Long)
}