package com.tamersarioglu.pairedup.presentation.screens.scores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.Score
import com.tamersarioglu.pairedup.presentation.components.EmptyState
import com.tamersarioglu.pairedup.presentation.components.LoadingScreen
import com.tamersarioglu.pairedup.presentation.components.ScoreItem
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScoresViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    if (uiState.isLoading) {
        LoadingScreen(message = "Skorlar yükleniyor...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🏆 Skorlarım",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Yenile"
                        )
                    }

                    if (uiState.scores.isNotEmpty()) {
                        IconButton(onClick = { viewModel.showDeleteAllDialog() }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Tümünü Sil",
                                tint = AccentRed
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!uiState.isEmpty) {
                // Filter Section
                FilterSection(
                    selectedDifficulty = uiState.selectedDifficulty,
                    onFilterChange = viewModel::filterByDifficulty,
                    totalScores = uiState.scores.size,
                    filteredScores = uiState.displayScores.size
                )

                // Statistics Card
                ScoreStatisticsCard(
                    scores = uiState.displayScores,
                    selectedDifficulty = uiState.selectedDifficulty
                )
            }

            // Scores List
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (uiState.isEmpty) {
                    EmptyState(
                        title = "Henüz Skor Yok",
                        description = "İlk oyununuzu oynayın ve skorunuzu buraya kaydedin!",
                        icon = Icons.Default.EmojiEvents,
                        actionButton = {
                            OutlinedButton(
                                onClick = onNavigateBack
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Oyun Oyna")
                            }
                        }
                    )
                } else if (uiState.displayScores.isEmpty()) {
                    EmptyState(
                        title = "Filtrede Skor Bulunamadı",
                        description = "Seçilen zorluk seviyesinde henüz skor yok.",
                        icon = Icons.Default.FilterList,
                        actionButton = {
                            TextButton(
                                onClick = { viewModel.filterByDifficulty(null) }
                            ) {
                                Text("Filtreyi Temizle")
                            }
                        }
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        itemsIndexed(
                            items = uiState.displayScores,
                            key = { _, score -> score.id }
                        ) { index, score ->
                            ScoreItem(
                                score = score,
                                rank = index + 1,
                                onDeleteClick = {
                                    viewModel.deleteScore(score.id.toLong())
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete All Confirmation Dialog
    if (uiState.showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteAllDialog() },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = null,
                        tint = AccentRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🗑️ Tüm Skorları Sil")
                }
            },
            text = {
                Text("Tüm skorları silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.")
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteAllScores() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text("Tümünü Sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDeleteAllDialog() }) {
                    Text("İptal")
                }
            }
        )
    }

    // Error handling
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(5000)
            viewModel.clearError()
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AccentRed.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = AccentRed
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = error,
                    color = AccentRed,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.clearError() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = AccentRed
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    selectedDifficulty: GameDifficulty?,
    onFilterChange: (GameDifficulty?) -> Unit,
    totalScores: Int,
    filteredScores: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🔍 Filtreleme",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$filteredScores/$totalScores skor",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDifficulty == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Tümü") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.SelectAll,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                FilterChip(
                    selected = selectedDifficulty == GameDifficulty.EASY,
                    onClick = { onFilterChange(GameDifficulty.EASY) },
                    label = { Text("Kolay") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.SentimentSatisfied,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                FilterChip(
                    selected = selectedDifficulty == GameDifficulty.HARD,
                    onClick = { onFilterChange(GameDifficulty.HARD) },
                    label = { Text("Zor") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ScoreStatisticsCard(
    scores: List<Score>,
    selectedDifficulty: GameDifficulty?,
    modifier: Modifier = Modifier
) {
    if (scores.isEmpty()) return

    val avgScore = scores.map { it.score }.average().toInt()
    val bestScore = scores.maxByOrNull { it.score }?.score ?: 0
    val totalGames = scores.size
    val avgTime = scores.map { it.timeElapsed }.average().toInt()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = AccentGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = AccentGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "📊 İstatistikler",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (selectedDifficulty != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${selectedDifficulty.name})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "En İyi",
                    value = bestScore.toString(),
                    icon = Icons.Default.EmojiEvents
                )

                StatItem(
                    label = "Ortalama",
                    value = avgScore.toString(),
                    icon = Icons.AutoMirrored.Filled.TrendingUp
                )

                StatItem(
                    label = "Oyun",
                    value = totalGames.toString(),
                    icon = Icons.Default.Games
                )

                StatItem(
                    label = "Ort. Süre",
                    value = "${avgTime}s",
                    icon = Icons.Default.Timer
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = AccentGreen
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}