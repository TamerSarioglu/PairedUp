package com.tamersarioglu.pairedup.utils

object Constants {
    const val GAME_TIME_LIMIT = 60
    const val CARD_FLIP_DURATION = 600
    const val CARD_MATCH_DELAY = 1000L
    const val CARD_MISMATCH_DELAY = 1500L

    const val BASE_SCORE_PER_MATCH = 100
    const val TIME_BONUS_MULTIPLIER = 10
    const val ATTEMPT_PENALTY = 5

    const val MIN_CARD_NUMBER = 1
    const val MAX_CARD_NUMBER = 100

    const val DATABASE_NAME = "memory_game_database"
    const val SCORE_TABLE_NAME = "scores"

    const val SETTINGS_PREFERENCES = "settings_preferences"
    const val DARK_THEME_KEY = "dark_theme"
    const val TIMER_ENABLED_KEY = "timer_enabled"
    const val SOUND_ENABLED_KEY = "sound_enabled"
    const val VIBRATION_ENABLED_KEY = "vibration_enabled"
    const val GAME_TIME_LIMIT_KEY = "game_time_limit"

    const val CARD_ELEVATION = 8f
    const val CARD_CORNER_RADIUS = 12f
    const val GRID_SPACING = 8f
}