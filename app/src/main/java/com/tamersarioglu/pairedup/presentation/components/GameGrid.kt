package com.tamersarioglu.pairedup.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamersarioglu.pairedup.domain.model.Card
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.utils.Constants


@Composable
fun GameGrid(
    modifier: Modifier = Modifier,
    cards: List<Card>,
    difficulty: GameDifficulty,
    onCardClick: (Card) -> Unit,
    isEnabled: Boolean = true
) {
    val columns = when (difficulty) {
        GameDifficulty.EASY -> 4
        GameDifficulty.HARD -> 4
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier
            .fillMaxWidth()
            .padding(Constants.GRID_SPACING.dp),
        contentPadding = PaddingValues(Constants.GRID_SPACING.dp),
        verticalArrangement = Arrangement.spacedBy(Constants.GRID_SPACING.dp),
        horizontalArrangement = Arrangement.spacedBy(Constants.GRID_SPACING.dp)
    ) {
        items(
            items = cards,
            key = { card -> card.id }
        ) { card ->
            GameCard(
                modifier = modifier.fillMaxWidth(),
                card = card,
                onClick = { onCardClick(card) },
                isEnabled = isEnabled
            )
        }
    }
}