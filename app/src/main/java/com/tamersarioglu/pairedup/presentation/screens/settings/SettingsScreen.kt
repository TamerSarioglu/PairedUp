package com.tamersarioglu.pairedup.presentation.screens.settings


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamersarioglu.pairedup.presentation.components.LoadingScreen
import com.tamersarioglu.pairedup.presentation.components.settings.SettingsButton
import com.tamersarioglu.pairedup.presentation.components.settings.SettingsSwitch
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingScreen(message = "Ayarlar yükleniyor...")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "⚙️ Ayarlar",
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
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Theme Settings Section
            SettingsSection(
                title = "🎨 Görünüm Ayarları",
                icon = Icons.Default.Palette
            ) {
                SettingsSwitch(
                    title = "Koyu Tema",
                    description = "Uygulamayı koyu temada kullan",
                    icon = Icons.Default.DarkMode,
                    checked = uiState.settings.isDarkTheme,
                    onCheckedChange = viewModel::updateDarkTheme
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game Settings Section
            SettingsSection(
                title = "🎮 Oyun Ayarları",
                icon = Icons.Default.Games
            ) {
                SettingsSwitch(
                    title = "Zamanlayıcı",
                    description = "Oyun süresini sınırla (60 saniye)",
                    icon = Icons.Default.Timer,
                    checked = uiState.settings.isTimerEnabled,
                    onCheckedChange = viewModel::updateTimerEnabled
                )

                SettingsSwitch(
                    title = "Ses Efektleri",
                    description = "Oyun seslerini aç/kapat",
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    checked = uiState.settings.isSoundEnabled,
                    onCheckedChange = viewModel::updateSoundEnabled
                )

                SettingsSwitch(
                    title = "Titreşim",
                    description = "Dokunsal geri bildirim",
                    icon = Icons.Default.Vibration,
                    checked = uiState.settings.isVibrationEnabled,
                    onCheckedChange = viewModel::updateVibrationEnabled
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Data Management Section
            SettingsSection(
                title = "🗂️ Veri Yönetimi",
                icon = Icons.Default.Storage
            ) {
                SettingsButton(
                    title = "Skorları Sil",
                    description = "Tüm kayıtlı skorları temizle",
                    icon = Icons.Default.DeleteSweep,
                    onClick = { viewModel.clearScores() },
                    showArrow = false
                )

                SettingsButton(
                    title = "Ayarları Sıfırla",
                    description = "Tüm ayarları varsayılan değerlere döndür",
                    icon = Icons.Default.RestartAlt,
                    onClick = { viewModel.showResetDialog() },
                    showArrow = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Info Section
            SettingsSection(
                title = "ℹ️ Uygulama Bilgisi",
                icon = Icons.Default.Info
            ) {
                AppInfoCard()
            }

            // Success/Error Messages
            uiState.successMessage?.let { message ->
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearSuccessMessage()
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentGreen.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentGreen
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = message,
                            color = AccentGreen,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.clearSuccessMessage() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Kapat",
                                tint = AccentGreen
                            )
                        }
                    }
                }
            }

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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Reset Settings Confirmation Dialog
    if (uiState.showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideResetDialog() },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = null,
                        tint = AccentRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🔄 Ayarları Sıfırla")
                }
            },
            text = {
                Text("Tüm ayarları varsayılan değerlere sıfırlamak istediğinizden emin misiniz?")
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetAllSettings() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text("Sıfırla")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideResetDialog() }) {
                    Text("İptal")
                }
            }
        )
    }

    // Loading overlay for saving operations
    if (uiState.isSaving) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Kaydediliyor...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Section Content
        Column {
            content()
        }
    }
}

@Composable
private fun AppInfoCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
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
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "🧠 Memory Game",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Versiyon 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Android Bootcamp bitirme projesi olarak geliştirilmiştir.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    icon = Icons.Default.Code,
                    label = "Kotlin",
                    value = "1.9.20"
                )
                InfoItem(
                    icon = Icons.Default.Android,
                    label = "Compose",
                    value = "2023.10"
                )
                InfoItem(
                    icon = Icons.Default.Architecture,
                    label = "MVVM",
                    value = "Clean"
                )
            }
        }
    }
}

@Composable
private fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}