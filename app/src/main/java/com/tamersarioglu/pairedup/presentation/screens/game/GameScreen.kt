package com.tamersarioglu.pairedup.presentation.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.domain.model.GameStatus
import com.tamersarioglu.pairedup.presentation.components.GameGrid
import com.tamersarioglu.pairedup.presentation.components.GameHeader
import com.tamersarioglu.pairedup.presentation.components.GameResultDialog
import com.tamersarioglu.pairedup.presentation.components.LoadingScreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed
import com.tamersarioglu.pairedup.utils.GameSoundManager
import com.tamersarioglu.pairedup.utils.VibrationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    playerName: String,
    difficulty: GameDifficulty,
    onNavigateBack: () -> Unit,
    onNavigateToScores: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showPauseDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    val soundManager = remember { GameSoundManager(context) }
    val vibrationManager = remember { VibrationManager(context) }

    // Initialize game
    LaunchedEffect(playerName, difficulty) {
        viewModel.initializeGame(playerName, difficulty)
        soundManager.initialize()
    }

    // Handle back button
    BackHandler {
        if (uiState.gameState.gameStatus == GameStatus.PLAYING) {
            showExitDialog = true
        } else {
            onNavigateBack()
        }
    }

    // Show loading screen
    if (uiState.isLoading) {
        LoadingScreen(message = "Oyun yükleniyor...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🎮 Memory Game",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (uiState.gameState.gameStatus == GameStatus.PLAYING) {
                                showExitDialog = true
                            } else {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    if (uiState.gameState.gameStatus == GameStatus.PLAYING) {
                        IconButton(
                            onClick = {
                                viewModel.pauseGame()
                                showPauseDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Duraklat"
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
            // Game Header
            GameHeader(
                playerName = uiState.gameState.playerName,
                score = uiState.gameState.score,
                timeLeft = uiState.gameState.timeLeft,
                showTimer = uiState.gameState.isTimerEnabled
            )

            // Game Status Info
            GameStatusBar(
                gameStatus = uiState.gameState.gameStatus,
                matchedPairs = uiState.gameState.matchedPairs,
                totalPairs = uiState.gameState.difficulty.uniqueNumbers,
                attempts = uiState.gameState.attempts
            )

            // Game Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GameGrid(
                    cards = uiState.gameState.cards,
                    difficulty = uiState.gameState.difficulty,
                    onCardClick = { card ->
                        viewModel.onCardClick(card)
                        // Add haptic feedback
                        vibrationManager.vibrateOnMatch()
                    },
                    isEnabled = uiState.canInteract
                )

                // Overlay for paused state
                if (uiState.gameState.gameStatus == GameStatus.PAUSED) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Pause,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "⏸️ Oyun Duraklatıldı",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Devam etmek için butona basın",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            viewModel.resumeGame()
                                            showPauseDialog = false
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Devam Et")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Game Result Dialog
    if (uiState.showResultDialog) {
        GameResultDialog(
            isWon = uiState.gameState.gameStatus == GameStatus.WON,
            score = uiState.gameState.score,
            timeElapsed = if (uiState.gameState.isTimerEnabled) {
                60 - uiState.gameState.timeLeft
            } else {
                uiState.gameState.attempts * 2 // Estimate based on attempts
            },
            attempts = uiState.gameState.attempts,
            difficulty = uiState.gameState.difficulty,
            onSaveScore = {
                viewModel.saveScore()
            },
            onPlayAgain = {
                viewModel.resetGame()
            },
            onGoHome = {
                viewModel.dismissResultDialog()
                onNavigateBack()
            }
        )
    }

    // Pause Dialog
    if (showPauseDialog) {
        AlertDialog(
            onDismissRequest = { /* Prevent dismissal */ },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("⏸️ Oyun Duraklatıldı")
                }
            },
            text = {
                Text("Oyunu devam ettirmek istiyor musunuz?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resumeGame()
                        showPauseDialog = false
                    }
                ) {
                    Text("Devam Et")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPauseDialog = false
                        showExitDialog = true
                    }
                ) {
                    Text("Çıkış")
                }
            }
        )
    }

    // Exit Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = AccentRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🚪 Oyundan Çık")
                }
            },
            text = {
                Text("Oyundan çıkmak istediğinizden emin misiniz? İlerlemeniz kaybedilecek.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text("Çık")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("İptal")
                }
            }
        )
    }

    // Error Snackbar
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }

    // Score saved success
    if (uiState.scoreSaved) {
        LaunchedEffect(Unit) {
            onNavigateToScores()
        }
    }
}

@Composable
private fun GameStatusBar(
    gameStatus: GameStatus,
    matchedPairs: Int,
    totalPairs: Int,
    attempts: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (gameStatus) {
                GameStatus.PLAYING -> MaterialTheme.colorScheme.surfaceVariant
                GameStatus.WON -> AccentGreen.copy(alpha = 0.2f)
                GameStatus.LOST -> AccentRed.copy(alpha = 0.2f)
                GameStatus.PAUSED -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$matchedPairs/$totalPairs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )
                Text(
                    text = "Eşleşen",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )

            // Attempts
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = attempts.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Deneme",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val (statusText, statusColor) = when (gameStatus) {
                    GameStatus.PLAYING -> "🎮 Oynuyor" to AccentGreen
                    GameStatus.WON -> "🏆 Kazandın!" to AccentGreen
                    GameStatus.LOST -> "⏰ Süre Doldu" to AccentRed
                    GameStatus.PAUSED -> "⏸️ Durduruldu" to MaterialTheme.colorScheme.primary
                    else -> "🎯 Hazır" to MaterialTheme.colorScheme.primary
                }

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )

                // Progress bar for matched pairs
                LinearProgressIndicator(
                    progress = { if (totalPairs > 0) matchedPairs.toFloat() / totalPairs else 0f },
                    modifier = Modifier
                        .width(60.dp)
                        .padding(top = 4.dp),
                    color = AccentGreen,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
            }
        }
    }
}