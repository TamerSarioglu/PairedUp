package com.tamersarioglu.pairedup.presentation.screens.home

data class HomeUiState(
    val isLoading: Boolean = false,
    val hasScores: Boolean = false,
    val recentHighScore: Int = 0,
    val error: String? = null
)