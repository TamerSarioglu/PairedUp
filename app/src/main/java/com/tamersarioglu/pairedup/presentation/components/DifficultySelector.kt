package com.tamersarioglu.pairedup.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed

@Composable
fun DifficultySelector(
    modifier: Modifier = Modifier,
    selectedDifficulty: GameDifficulty,
    onDifficultySelected: (GameDifficulty) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Zorluk Seviyesi",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DifficultyCard(
                difficulty = GameDifficulty.EASY,
                isSelected = selectedDifficulty == GameDifficulty.EASY,
                onClick = { onDifficultySelected(GameDifficulty.EASY) },
                modifier = modifier.weight(1f)
            )

            DifficultyCard(
                difficulty = GameDifficulty.HARD,
                isSelected = selectedDifficulty == GameDifficulty.HARD,
                onClick = { onDifficultySelected(GameDifficulty.HARD) },
                modifier = modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DifficultyCard(
    modifier: Modifier = Modifier,
    difficulty: GameDifficulty,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (title, description, icon, color) = when (difficulty) {
        GameDifficulty.EASY -> {
            val title = "Kolay"
            val description = "16 kart\n(4x4 grid)"
            val icon = Icons.Default.SentimentSatisfied
            val color = AccentGreen
            arrayOf(title, description, icon, color)
        }
        GameDifficulty.HARD -> {
            val title = "Zor"
            val description = "24 kart\n(4x6 grid)"
            val icon = Icons.Default.FlashOn
            val color = AccentRed
            arrayOf(title, description, icon, color)
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) color as Color else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(300),
        label = "card_color"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) color as Color else Color.Transparent,
        animationSpec = tween(300),
        label = "border_color"
    )

    Card(
        modifier = modifier
            .aspectRatio(0.8f)
            .selectable(
                selected = isSelected,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = animatedColor.copy(alpha = if (isSelected) 0.2f else 1f)
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, borderColor)
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon as ImageVector,
                contentDescription = null,
                tint = if (isSelected) color as Color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier.size(40.dp)
            )

            Spacer(modifier = modifier.height(12.dp))

            Text(
                text = title as String,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color as Color else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = description as String,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}