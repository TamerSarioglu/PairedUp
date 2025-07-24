package com.tamersarioglu.pairedup.utils

import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

fun <T> List<T>.shuffleWithSeed(seed: Long = System.currentTimeMillis()): List<T> {
    return this.shuffled(Random(seed))
}

fun IntRange.randomList(count: Int): List<Int> {
    return (1..count).map { this.random() }.distinct().take(count)
}

fun Int.formatTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format(Locale.US,"%02d:%02d", minutes, seconds)
}

fun calculateGameScore(
    matchedPairs: Int,
    timeElapsed: Int,
    attempts: Int,
    totalPairs: Int
): Int {
    val baseScore = matchedPairs * Constants.BASE_SCORE_PER_MATCH
    val timeBonus = if (timeElapsed < Constants.GAME_TIME_LIMIT) {
        (Constants.GAME_TIME_LIMIT - timeElapsed) * Constants.TIME_BONUS_MULTIPLIER
    } else 0
    val attemptPenalty = (attempts - totalPairs) * Constants.ATTEMPT_PENALTY

    return maxOf(0, baseScore + timeBonus - attemptPenalty)
}

suspend fun delayedExecution(delayMs: Long, action: suspend () -> Unit) {
    delay(delayMs)
    action()
}