package com.tamersarioglu.pairedup.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentBlue
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentOrange
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed
import com.tamersarioglu.pairedup.utils.formatTime

@Composable
fun GameResultDialog(
    isWon: Boolean,
    score: Int,
    timeElapsed: Int,
    attempts: Int,
    difficulty: GameDifficulty,
    onSaveScore: () -> Unit,
    onPlayAgain: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { /* Prevent dismissal */ }) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Result Icon and Title
                if (isWon) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "🎉 Tebrikler! 🎉",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Tüm kartları eşleştirdin!",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = AccentRed,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "⏰ Süre Doldu ⏰",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentRed,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Bir dahaki sefere daha hızlı ol!",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Game Statistics
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        GameStatRow(
                            icon = Icons.Default.Star,
                            label = "Skor",
                            value = score.toString(),
                            color = AccentGreen
                        )

                        GameStatRow(
                            icon = Icons.Default.Timer,
                            label = "Süre",
                            value = timeElapsed.formatTime(),
                            color = AccentBlue
                        )

                        GameStatRow(
                            icon = Icons.Default.TouchApp,
                            label = "Deneme",
                            value = attempts.toString(),
                            color = AccentOrange
                        )

                        GameStatRow(
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            label = "Zorluk",
                            value = when (difficulty) {
                                GameDifficulty.EASY -> "Kolay"
                                GameDifficulty.HARD -> "Zor"
                            },
                            color = MaterialTheme.colorScheme.primary,
                            isLast = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                if (isWon) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onSaveScore,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Kaydet")
                        }

                        Button(
                            onClick = onPlayAgain,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tekrar")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = onGoHome,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ana Sayfa")
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onGoHome,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ana Sayfa")
                        }

                        Button(
                            onClick = onPlayAgain,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tekrar Oyna")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameStatRow(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    isLast: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }

    if (!isLast) {
        Spacer(modifier = Modifier.height(12.dp))
    }
}