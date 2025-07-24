package com.tamersarioglu.pairedup.data.repository

import com.tamersarioglu.pairedup.data.local.database.dao.ScoreDao
import com.tamersarioglu.pairedup.data.mapper.ScoreMapper
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoreRepositoryImpl @Inject constructor(
    private val scoreDao: ScoreDao
) : ScoreRepository {

    override fun getAllScores(): Flow<List<Score>> {
        return scoreDao.getAllScores().map { entities ->
            ScoreMapper.toDomainList(entities)
        }
    }

    override fun getScoresByDifficulty(difficulty: GameDifficulty): Flow<List<Score>> {
        return scoreDao.getScoresByDifficulty(difficulty.name).map { entities ->
            ScoreMapper.toDomainList(entities)
        }
    }

    override fun getTopScores(limit: Int): Flow<List<Score>> {
        return scoreDao.getTopScores(limit).map { entities ->
            ScoreMapper.toDomainList(entities)
        }
    }

    override fun getScoresByPlayer(playerName: String): Flow<List<Score>> {
        return scoreDao.getScoresByPlayer(playerName).map { entities ->
            ScoreMapper.toDomainList(entities)
        }
    }

    override suspend fun getHighestScoreByDifficulty(difficulty: GameDifficulty): Int? {
        return scoreDao.getHighestScoreByDifficulty(difficulty.name)
    }

    override suspend fun getScoreCount(): Int {
        return scoreDao.getScoreCount()
    }

    override suspend fun insertScore(score: Score): Long {
        return scoreDao.insertScore(ScoreMapper.toEntity(score))
    }

    override suspend fun insertScores(scores: List<Score>) {
        scoreDao.insertScores(ScoreMapper.toEntityList(scores))
    }

    override suspend fun updateScore(score: Score) {
        scoreDao.updateScore(ScoreMapper.toEntity(score))
    }

    override suspend fun deleteScore(score: Score) {
        scoreDao.deleteScore(ScoreMapper.toEntity(score))
    }

    override suspend fun deleteAllScores() {
        scoreDao.deleteAllScores()
    }

    override suspend fun deleteScoresByDifficulty(difficulty: GameDifficulty) {
        scoreDao.deleteScoresByDifficulty(difficulty.name)
    }

    override suspend fun deleteScoreById(scoreId: Long) {
        scoreDao.deleteScoreById(scoreId)
    }
}