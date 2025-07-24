package com.tamersarioglu.pairedup.domain.repository

import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    fun getAllScores(): Flow<List<Score>>

    fun getScoresByDifficulty(difficulty: GameDifficulty): Flow<List<Score>>

    fun getTopScores(limit: Int): Flow<List<Score>>

    fun getScoresByPlayer(playerName: String): Flow<List<Score>>

    suspend fun getHighestScoreByDifficulty(difficulty: GameDifficulty): Int?

    suspend fun getScoreCount(): Int

    suspend fun insertScore(score: Score): Long

    suspend fun insertScores(scores: List<Score>)

    suspend fun updateScore(score: Score)

    suspend fun deleteScore(score: Score)

    suspend fun deleteAllScores()

    suspend fun deleteScoresByDifficulty(difficulty: GameDifficulty)

    suspend fun deleteScoreById(scoreId: Long)
}