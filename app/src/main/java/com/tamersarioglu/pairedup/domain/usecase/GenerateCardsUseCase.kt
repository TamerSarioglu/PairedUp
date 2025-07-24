package com.tamersarioglu.pairedup.domain.usecase

import com.tamersarioglu.pairedup.domain.model.Card
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.utils.Constants
import com.tamersarioglu.pairedup.utils.shuffleWithSeed
import javax.inject.Inject
import kotlin.random.Random

class GenerateCardsUseCase @Inject constructor() {

    fun generateCards(difficulty: GameDifficulty): List<Card> {
        val uniqueNumbers = generateUniqueNumbers(difficulty.uniqueNumbers)
        val cardPairs = createCardPairs(uniqueNumbers)
        return shuffleCards(cardPairs)
    }

    private fun generateUniqueNumbers(count: Int): List<Int> {
        val numbers = mutableSetOf<Int>()
        while (numbers.size < count) {
            numbers.add(Random.nextInt(Constants.MIN_CARD_NUMBER, Constants.MAX_CARD_NUMBER + 1))
        }
        return numbers.toList()
    }

    private fun createCardPairs(numbers: List<Int>): List<Card> {
        val cards = mutableListOf<Card>()
        var cardId = 0

        numbers.forEach { number ->

            cards.add(
                Card(
                    id = cardId++,
                    number = number,
                    position = -1
                )
            )
            cards.add(
                Card(
                    id = cardId++,
                    number = number,
                    position = -1
                )
            )
        }

        return cards
    }

    private fun shuffleCards(cards: List<Card>): List<Card> {
        return cards.shuffleWithSeed().mapIndexed { index, card ->
            card.copy(position = index)
        }
    }
}