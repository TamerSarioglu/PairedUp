package com.tamersarioglu.pairedup.presentation.screens.home


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamersarioglu.pairedup.presentation.components.LoadingScreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToGameSetup: () -> Unit,
    onNavigateToScores: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (uiState.isLoading) {
        LoadingScreen(message = "Ana sayfa yükleniyor...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🧠 Memory Game",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Yenile"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = modifier.height(32.dp))

            WelcomeSection(
                hasScores = uiState.hasScores,
                recentHighScore = uiState.recentHighScore
            )

            Spacer(modifier = modifier.height(24.dp))

            MenuButtons(
                onNavigateToGameSetup = onNavigateToGameSetup,
                onNavigateToScores = onNavigateToScores,
                onNavigateToSettings = onNavigateToSettings,
                hasScores = uiState.hasScores
            )

            Spacer(modifier = modifier.height(32.dp))

            GameRulesCard()

            uiState.error?.let { error ->
                LaunchedEffect(error) {
                    kotlinx.coroutines.delay(5000)
                    viewModel.clearError()
                }

                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentRed.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = AccentRed
                        )
                        Spacer(modifier = modifier.width(12.dp))
                        Text(
                            text = error,
                            color = AccentRed,
                            modifier = modifier.weight(1f)
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

            Spacer(modifier = modifier.height(16.dp))
        }
    }
}

@Composable
private fun WelcomeSection(
    modifier: Modifier = Modifier,
    hasScores: Boolean,
    recentHighScore: Int
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎮 Memory Game'e Hoş Geldin!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = "Kartları eşleştir ve hafızanı test et!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            if (hasScores && recentHighScore > 0) {
                Spacer(modifier = modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = modifier.size(20.dp)
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = "En Yüksek Skor: $recentHighScore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = AccentGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuButtons(
    modifier: Modifier = Modifier,
    onNavigateToGameSetup: () -> Unit,
    onNavigateToScores: () -> Unit,
    onNavigateToSettings: () -> Unit,
    hasScores: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onNavigateToGameSetup,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen
            )
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = modifier.size(24.dp)
            )
            Spacer(modifier = modifier.width(12.dp))
            Text(
                text = "🚀 Oyunu Başlat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedButton(
            onClick = onNavigateToScores,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Leaderboard,
                contentDescription = null,
                modifier = modifier.size(24.dp)
            )
            Spacer(modifier = modifier.width(12.dp))
            Text(
                text = if (hasScores) "🏆 Skorlarım" else "📊 Skorlar (Boş)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedButton(
            onClick = onNavigateToSettings,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = modifier.size(24.dp)
            )
            Spacer(modifier = modifier.width(12.dp))
            Text(
                text = "⚙️ Ayarlar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GameRulesCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.width(12.dp))
                Text(
                    text = "🎯 Nasıl Oynanır?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = modifier.height(16.dp))

            val rules = listOf(
                "🔢 Kartlarda gizli sayılar var",
                "🎯 Aynı sayılı kartları eşleştir",
                "⏰ Süre dolmadan tamamla",
                "🏆 Yüksek skor için hızlı ol",
                "🎮 İki zorluk seviyesi: Kolay (16 kart), Zor (24 kart)"
            )

            rules.forEach { rule ->
                Row(
                    modifier = modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = modifier.padding(end = 8.dp, top = 2.dp)
                    )
                    Text(
                        text = rule,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}