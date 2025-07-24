package com.tamersarioglu.pairedup.domain.usecase

import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.domain.repository.ScoreRepository
import javax.inject.Inject

class SaveScoreUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository
) {

    suspend fun saveScore(score: Score): Long {
        return scoreRepository.insertScore(score)
    }

    suspend fun deleteAllScores() {
        scoreRepository.deleteAllScores()
    }

    suspend fun deleteScoreById(scoreId: Long) {
        scoreRepository.deleteScoreById(scoreId)
    }
}