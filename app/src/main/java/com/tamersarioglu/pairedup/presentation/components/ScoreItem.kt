package com.tamersarioglu.pairedup.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentBlue
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentOrange
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed
import com.tamersarioglu.pairedup.utils.formatTime
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScoreItem(
    score: Score,
    rank: Int,
    onDeleteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (rank <= 3) {
                when (rank) {
                    1 -> AccentGreen.copy(alpha = 0.1f)
                    2 -> AccentOrange.copy(alpha = 0.1f)
                    3 -> AccentBlue.copy(alpha = 0.1f)
                    else -> MaterialTheme.colorScheme.surface
                }
            } else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        when (rank) {
                            1 -> AccentGreen
                            2 -> AccentOrange
                            3 -> AccentBlue
                            else -> MaterialTheme.colorScheme.primary
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = score.playerName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${score.score} puan",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = score.timeElapsed.formatTime(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DifficultyChip(difficulty = score.difficulty)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = formatDate(score.timestamp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (onDeleteClick != null) {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Skoru Sil",
                        tint = AccentRed
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Skoru Sil") },
            text = { Text("Bu skoru silmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick?.invoke()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Sil", color = AccentRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
private fun DifficultyChip(
    difficulty: GameDifficulty,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (difficulty) {
        GameDifficulty.EASY -> "Kolay" to AccentGreen
        GameDifficulty.HARD -> "Zor" to AccentRed
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 12.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(date)
}