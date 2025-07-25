package com.tamersarioglu.pairedup.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.pairedup.domain.model.Card
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.GameState
import com.tamersarioglu.pairedup.domain.model.GameStatus
import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.domain.usecase.CalculateScoreUseCase
import com.tamersarioglu.pairedup.domain.usecase.GenerateCardsUseCase
import com.tamersarioglu.pairedup.domain.usecase.GetSettingsUseCase
import com.tamersarioglu.pairedup.domain.usecase.SaveScoreUseCase
import com.tamersarioglu.pairedup.utils.Constants
import com.tamersarioglu.pairedup.utils.delayedExecution
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val generateCardsUseCase: GenerateCardsUseCase,
    private val calculateScoreUseCase: CalculateScoreUseCase,
    private val saveScoreUseCase: SaveScoreUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var cardFlipJob: Job? = null

    fun initializeGame(playerName: String, difficulty: GameDifficulty) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val settings = getSettingsUseCase.getGameSettings().first()
                val cards = generateCardsUseCase.generateCards(difficulty)
                val gameState = GameState(
                    cards = cards,
                    difficulty = difficulty,
                    playerName = playerName,
                    isTimerEnabled = settings.isTimerEnabled,
                    gameStatus = GameStatus.PLAYING,
                    timeLeft = if (settings.isTimerEnabled) settings.gameTimeLimit else 0
                )

                _uiState.update {
                    it.copy(
                        gameState = gameState,
                        isLoading = false,
                        error = null
                    )
                }

                if (settings.isTimerEnabled) {
                    startTimer()
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Oyun başlatılırken hata: ${e.message}"
                    )
                }
            }
        }
    }

    fun onCardClick(clickedCard: Card) {
        val currentState = _uiState.value

        if (!currentState.canInteract ||
            clickedCard.isFlipped ||
            clickedCard.isMatched ||
            currentState.gameState.flippedCards.size >= 2) {
            return
        }

        cardFlipJob?.cancel()

        val updatedCards = currentState.gameState.cards.map { card ->
            if (card.id == clickedCard.id) {
                card.copy(isFlipped = true)
            } else card
        }

        val flippedCards = updatedCards.filter { it.isFlipped && !it.isMatched }
        val newAttempts = if (flippedCards.size == 2) {
            currentState.gameState.attempts + 1
        } else {
            currentState.gameState.attempts
        }

        _uiState.update {
            it.copy(
                gameState = it.gameState.copy(
                    cards = updatedCards,
                    flippedCards = flippedCards,
                    attempts = newAttempts
                )
            )
        }

        if (flippedCards.size == 2) {
            checkForMatch(flippedCards)
        }
    }

    private fun checkForMatch(flippedCards: List<Card>) {
        val card1 = flippedCards[0]
        val card2 = flippedCards[1]
        val isMatch = card1.number == card2.number

        cardFlipJob = viewModelScope.launch {
            if (isMatch) {
                delayedExecution(Constants.CARD_MATCH_DELAY) {
                    handleMatch(card1, card2)
                }
            } else {
                delayedExecution(Constants.CARD_MISMATCH_DELAY) {
                    handleMismatch(card1, card2)
                }
            }
        }
    }

    private fun handleMatch(card1: Card, card2: Card) {
        val currentState = _uiState.value.gameState

        val updatedCards = currentState.cards.map { card ->
            if (card.id == card1.id || card.id == card2.id) {
                card.copy(isMatched = true, isFlipped = true)
            } else card
        }

        val matchedPairs = currentState.matchedPairs + 1
        val totalPairs = currentState.difficulty.uniqueNumbers

        val timeElapsed = if (currentState.isTimerEnabled) {
            Constants.GAME_TIME_LIMIT - currentState.timeLeft
        } else 0

        val score = calculateScoreUseCase.calculateScore(
            matchedPairs = matchedPairs,
            timeElapsed = timeElapsed,
            attempts = currentState.attempts,
            totalPairs = totalPairs
        )

        val gameStatus = if (matchedPairs >= totalPairs) {
            GameStatus.WON
        } else {
            GameStatus.PLAYING
        }

        _uiState.update {
            it.copy(
                gameState = it.gameState.copy(
                    cards = updatedCards,
                    flippedCards = emptyList(),
                    matchedPairs = matchedPairs,
                    score = score,
                    gameStatus = gameStatus
                ),
                showResultDialog = gameStatus == GameStatus.WON
            )
        }

        if (gameStatus == GameStatus.WON) {
            stopTimer()
        }
    }

    private fun handleMismatch(card1: Card, card2: Card) {
        val currentState = _uiState.value.gameState

        val updatedCards = currentState.cards.map { card ->
            if (card.id == card1.id || card.id == card2.id) {
                card.copy(isFlipped = false)
            } else card
        }

        _uiState.update {
            it.copy(
                gameState = it.gameState.copy(
                    cards = updatedCards,
                    flippedCards = emptyList()
                )
            )
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_uiState.value.gameState.timeLeft > 0 &&
                _uiState.value.gameState.gameStatus == GameStatus.PLAYING) {
                delay(1000)

                val newTimeLeft = _uiState.value.gameState.timeLeft - 1

                _uiState.update {
                    it.copy(
                        gameState = it.gameState.copy(timeLeft = newTimeLeft)
                    )
                }

                if (newTimeLeft <= 0) {
                    handleTimeUp()
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun handleTimeUp() {
        stopTimer()
        _uiState.update {
            it.copy(
                gameState = it.gameState.copy(gameStatus = GameStatus.LOST),
                showResultDialog = true
            )
        }
    }

    fun saveScore() {
        viewModelScope.launch {
            try {
                val gameState = _uiState.value.gameState
                val timeElapsed = if (gameState.isTimerEnabled) {
                    Constants.GAME_TIME_LIMIT - gameState.timeLeft
                } else 0

                val score = Score(
                    playerName = gameState.playerName,
                    score = gameState.score,
                    difficulty = gameState.difficulty,
                    timeElapsed = timeElapsed,
                    attempts = gameState.attempts
                )

                saveScoreUseCase.saveScore(score)

                _uiState.update {
                    it.copy(scoreSaved = true)
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Skor kaydedilirken hata: ${e.message}")
                }
            }
        }
    }

    fun resetGame() {
        stopTimer()
        cardFlipJob?.cancel()

        val currentState = _uiState.value.gameState
        initializeGame(currentState.playerName, currentState.difficulty)

        _uiState.update {
            it.copy(
                showResultDialog = false,
                scoreSaved = false,
                error = null
            )
        }
    }

    fun dismissResultDialog() {
        _uiState.update {
            it.copy(showResultDialog = false)
        }
    }

    fun pauseGame() {
        if (_uiState.value.gameState.gameStatus == GameStatus.PLAYING) {
            stopTimer()
            _uiState.update {
                it.copy(
                    gameState = it.gameState.copy(gameStatus = GameStatus.PAUSED)
                )
            }
        }
    }

    fun resumeGame() {
        if (_uiState.value.gameState.gameStatus == GameStatus.PAUSED) {
            _uiState.update {
                it.copy(
                    gameState = it.gameState.copy(gameStatus = GameStatus.PLAYING)
                )
            }

            if (_uiState.value.gameState.isTimerEnabled) {
                startTimer()
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        cardFlipJob?.cancel()
    }
}