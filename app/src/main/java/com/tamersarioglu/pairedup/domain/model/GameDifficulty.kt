package com.tamersarioglu.pairedup.domain.model

enum class GameDifficulty (val cardCount:Int, val uniqueNumbers:Int){
    EASY(16, 8),
    HARD(24, 12)
}