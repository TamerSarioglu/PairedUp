package com.tamersarioglu.pairedup.domain.model

data class Card(
    val id: Int,
    val number: Int,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false,
    val position: Int
)