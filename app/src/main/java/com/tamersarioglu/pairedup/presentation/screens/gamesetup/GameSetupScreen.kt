package com.tamersarioglu.pairedup.presentation.screens.gamesetup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamersarioglu.pairedup.domain.model.GameDifficulty
import com.tamersarioglu.pairedup.presentation.components.DifficultySelector
import com.tamersarioglu.pairedup.presentation.components.LoadingScreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onStartGame: (String, GameDifficulty) -> Unit,
    viewModel: GameSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    if (uiState.isLoading) {
        LoadingScreen(message = "Oyun hazırlanıyor...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🎮 Oyun Kurulumu",
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
                }
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = modifier.size(18.dp)
                        )
                        Spacer(modifier = modifier.width(8.dp))
                        Text("İptal")
                    }

                    Button(
                        onClick = {
                            if (viewModel.validateAndPrepareGame()) {
                                keyboardController?.hide()
                                onStartGame(uiState.playerName.trim(), uiState.selectedDifficulty)
                            }
                        },
                        enabled = uiState.canStartGame,
                        modifier = modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = modifier.size(18.dp)
                        )
                        Spacer(modifier = modifier.width(8.dp))
                        Text("Başlat!")
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PlayerNameSection(
                playerName = uiState.playerName,
                isValid = uiState.isPlayerNameValid,
                onNameChange = viewModel::updatePlayerName,
                focusRequester = focusRequester,
                onImeAction = {
                    keyboardController?.hide()
                }
            )

            DifficultySelector(
                selectedDifficulty = uiState.selectedDifficulty,
                onDifficultySelected = viewModel::selectDifficulty
            )

            GamePreviewCard(
                difficulty = uiState.selectedDifficulty
            )

            uiState.error?.let { error ->
                Card(
                    modifier = modifier.fillMaxWidth(),
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
                        IconButton(onClick = viewModel::clearError) {
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
private fun PlayerNameSection(
    modifier: Modifier = Modifier,
    playerName: String,
    isValid: Boolean,
    onNameChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onImeAction: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "👤 Oyuncu Adı",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = playerName,
            onValueChange = onNameChange,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = { Text("Adınızı girin") },
            placeholder = { Text("Örn: Ahmet") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (playerName.isNotEmpty()) {
                    if (isValid) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentGreen
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = AccentRed
                        )
                    }
                }
            },
            isError = playerName.isNotEmpty() && !isValid,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() }
            ),
            singleLine = true
        )

        if (playerName.isNotEmpty() && !isValid) {
            Text(
                text = "⚠️ En az 2 karakter, sadece harf kullanın",
                color = AccentRed,
                fontSize = 12.sp,
                modifier = modifier.padding(start = 16.dp, top = 4.dp)
            )
        } else if (isValid) {
            Text(
                text = "✅ Oyuncu adı geçerli",
                color = AccentGreen,
                fontSize = 12.sp,
                modifier = modifier.padding(start = 16.dp, top = 4.dp)
            )
        } else {
            Text(
                text = "💡 İpucu: En az 2 karakter giriniz",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun GamePreviewCard(
    modifier: Modifier = Modifier,
    difficulty: GameDifficulty
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Preview,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(
                    text = "🎯 Oyun Önizleme",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = modifier.height(12.dp))

            val (gridSize, cardCount, estimatedTime) = when (difficulty) {
                GameDifficulty.EASY -> Triple("4x4", "16", "2-3")
                GameDifficulty.HARD -> Triple("4x6", "24", "4-5")
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GameInfoRow(
                    icon = Icons.Default.GridView,
                    label = "Grid Boyutu",
                    value = gridSize
                )

                GameInfoRow(
                    icon = Icons.Default.Style,
                    label = "Kart Sayısı",
                    value = "$cardCount kart"
                )

                GameInfoRow(
                    icon = Icons.Default.Timer,
                    label = "Tahmini Süre",
                    value = "$estimatedTime dakika"
                )

                GameInfoRow(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    label = "Zorluk",
                    value = when (difficulty) {
                        GameDifficulty.EASY -> "👶 Kolay"
                        GameDifficulty.HARD -> "🔥 Zor"
                    }
                )
            }
        }
    }
}

@Composable
private fun GameInfoRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.size(16.dp)
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            modifier = modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}