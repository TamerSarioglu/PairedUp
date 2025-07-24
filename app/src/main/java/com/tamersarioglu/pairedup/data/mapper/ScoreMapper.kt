package com.tamersarioglu.pairedup.data.mapper

import com.tamersarioglu.pairedup.data.local.database.entities.ScoreEntity
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score

object ScoreMapper {
    fun toEntity(score: Score): ScoreEntity {
        return ScoreEntity(
            id = score.id,
            playerName = score.playerName,
            score = score.score,
            difficulty = score.difficulty.toString(),
            timeElapsed = score.timeElapsed,
            attempts = score.attempts,
            timestamp = score.timestamp
        )
    }

    fun toDomain(entity: ScoreEntity): Score {
        return Score(
            id = entity.id,
            playerName = entity.playerName,
            score = entity.score,
            difficulty = GameDifficulty.valueOf(entity.difficulty),
            timeElapsed = entity.timeElapsed,
            attempts = entity.attempts,
            timestamp = entity.timestamp
        )
    }

    fun toDomainList(entities: List<ScoreEntity>): List<Score> {
        return entities.map { toDomain(it) }
    }

    fun toEntityList(scores: List<Score>): List<ScoreEntity> {
        return scores.map { toEntity(it) }
    }
}