package com.tamersarioglu.pairedup.domain.usecase

import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScoresUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository
){
    fun getAllScores(): Flow<List<Score>> {
        return scoreRepository.getAllScores()
    }

    fun getScoresByDifficulty(difficulty: GameDifficulty): Flow<List<Score>> {
        return scoreRepository.getScoresByDifficulty(difficulty)
    }

    fun getTopScores(limit: Int = 10): Flow<List<Score>> {
        return scoreRepository.getTopScores(limit)
    }

    fun getScoresByPlayer(playerName: String): Flow<List<Score>> {
        return scoreRepository.getScoresByPlayer(playerName)
    }

    suspend fun getHighestScoreByDifficulty(difficulty: GameDifficulty): Int? {
        return scoreRepository.getHighestScoreByDifficulty(difficulty)
    }
}