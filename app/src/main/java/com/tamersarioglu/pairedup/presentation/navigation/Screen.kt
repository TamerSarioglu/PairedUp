package com.tamersarioglu.pairedup.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object GameSetup : Screen()

    @Serializable
    data class Game(
        val playerName: String,
        val difficulty: String
    ) : Screen()

    @Serializable
    data object Scores : Screen()

    @Serializable
    data object Settings : Screen()
}