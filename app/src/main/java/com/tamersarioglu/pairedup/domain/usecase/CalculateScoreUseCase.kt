package com.tamersarioglu.pairedup.domain.usecase

import com.tamersarioglu.pairedup.utils.calculateGameScore
import javax.inject.Inject

class CalculateScoreUseCase @Inject constructor() {

    fun calculateScore(
        matchedPairs: Int,
        timeElapsed: Int,
        attempts: Int,
        totalPairs: Int
    ): Int {
        return calculateGameScore(
            matchedPairs = matchedPairs,
            timeElapsed = timeElapsed,
            attempts = attempts,
            totalPairs = totalPairs
        )
    }

    fun calculateTimeBonus(timeLeft: Int, totalTime: Int): Int {
        return if (timeLeft > 0) {
            (timeLeft * 10)
        } else 0
    }

    fun calculateEfficiencyBonus(attempts: Int, totalPairs: Int): Int {
        val perfectAttempts = totalPairs
        return if (attempts <= perfectAttempts) {
            (perfectAttempts - attempts + 1) * 50
        } else 0
    }
}