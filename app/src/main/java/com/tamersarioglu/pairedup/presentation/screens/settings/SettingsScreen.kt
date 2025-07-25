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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamersarioglu.pairedup.R
import com.tamersarioglu.pairedup.presentation.components.LoadingScreen
import com.tamersarioglu.pairedup.presentation.components.settings.SettingsButton
import com.tamersarioglu.pairedup.presentation.components.settings.SettingsSwitch
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentGreen
import com.tamersarioglu.pairedup.presentation.ui.theme.AccentRed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingScreen(message = stringResource(R.string.loading_settings))
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = modifier.height(8.dp))

            SettingsSection(
                title = stringResource(R.string.appearance_settings),
                icon = Icons.Default.Palette
            ) {
                SettingsSwitch(
                    title = stringResource(R.string.dark_theme),
                    description = stringResource(R.string.dark_theme_description),
                    icon = Icons.Default.DarkMode,
                    checked = uiState.settings.isDarkTheme,
                    onCheckedChange = viewModel::updateDarkTheme
                )
            }

            Spacer(modifier = modifier.height(16.dp))

            SettingsSection(
                title = stringResource(R.string.game_settings),
                icon = Icons.Default.Games
            ) {
                SettingsSwitch(
                    title = stringResource(R.string.timer),
                    description = stringResource(R.string.timer_description),
                    icon = Icons.Default.Timer,
                    checked = uiState.settings.isTimerEnabled,
                    onCheckedChange = viewModel::updateTimerEnabled
                )

                SettingsSwitch(
                    title = stringResource(R.string.sound_effects),
                    description = stringResource(R.string.sound_effects_description),
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    checked = uiState.settings.isSoundEnabled,
                    onCheckedChange = viewModel::updateSoundEnabled
                )

                SettingsSwitch(
                    title = stringResource(R.string.vibration),
                    description = stringResource(R.string.vibration_description),
                    icon = Icons.Default.Vibration,
                    checked = uiState.settings.isVibrationEnabled,
                    onCheckedChange = viewModel::updateVibrationEnabled
                )
            }

            Spacer(modifier = modifier.height(16.dp))

            SettingsSection(
                title = stringResource(R.string.data_management),
                icon = Icons.Default.Storage
            ) {
                SettingsButton(
                    title = stringResource(R.string.clear_scores),
                    description = stringResource(R.string.clear_scores_description),
                    icon = Icons.Default.DeleteSweep,
                    onClick = { viewModel.clearScores() },
                    showArrow = false
                )

                SettingsButton(
                    title = stringResource(R.string.reset_settings),
                    description = stringResource(R.string.reset_settings_description),
                    icon = Icons.Default.RestartAlt,
                    onClick = { viewModel.showResetDialog() },
                    showArrow = false
                )
            }

            Spacer(modifier = modifier.height(16.dp))

            SettingsSection(
                title = stringResource(R.string.app_info),
                icon = Icons.Default.Info
            ) {
                AppInfoCard()
            }

            uiState.successMessage?.let { message ->
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearSuccessMessage()
                }

                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentGreen.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentGreen
                        )
                        Spacer(modifier = modifier.width(12.dp))
                        Text(
                            text = message,
                            color = AccentGreen,
                            modifier = modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.clearSuccessMessage() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.close),
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
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                                contentDescription = stringResource(R.string.close),
                                tint = AccentRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = modifier.height(16.dp))
        }
    }

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
                    Spacer(modifier = modifier.width(8.dp))
                    Text(stringResource(R.string.reset_settings_dialog_title))
                }
            },
            text = {
                Text(stringResource(R.string.reset_settings_dialog_message))
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetAllSettings() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text(stringResource(R.string.reset))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideResetDialog() }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (uiState.isSaving) {
        Box(
            modifier = modifier
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
                    modifier = modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.saving),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier.size(20.dp)
            )
            Spacer(modifier = modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

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
            modifier = modifier
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
                    modifier = modifier.size(32.dp)
                )
                Spacer(modifier = modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.memory_game_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.version),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    icon = Icons.Default.Code,
                    label = stringResource(R.string.kotlin_label),
                    value = stringResource(R.string.kotlin_version)
                )
                InfoItem(
                    icon = Icons.Default.Android,
                    label = stringResource(R.string.compose_label),
                    value = stringResource(R.string.compose_version)
                )
                InfoItem(
                    icon = Icons.Default.Architecture,
                    label = stringResource(R.string.mvvm_label),
                    value = stringResource(R.string.mvvm_version)
                )
            }
        }
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.size(16.dp)
        )
        Spacer(modifier = modifier.height(4.dp))
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