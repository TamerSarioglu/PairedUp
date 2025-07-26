package com.tamersarioglu.pairedup.presentation.components.settings

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.tamersarioglu.pairedup.domain.model.ErrorType
import com.tamersarioglu.pairedup.domain.model.SettingState
import com.tamersarioglu.pairedup.utils.SettingsAnimationConstants
import com.tamersarioglu.pairedup.utils.AnimationHelpers

@Composable
private fun rememberSettingsAnimationState(
    settingState: SettingState,
    enabled: Boolean
): SettingsAnimationState {
    val loadingAlpha by animateFloatAsState(
        targetValue = if (settingState.isLoading) 1f else 0f,
        animationSpec = SettingsAnimationConstants.Specs.loadingFadeIn,
        label = "loadingAlpha"
    )
    
    val successScale by animateFloatAsState(
        targetValue = if (settingState.showSuccess) 1f else 0f,
        animationSpec = SettingsAnimationConstants.Specs.successScaleIn,
        label = "successScale"
    )
    
    val successAlpha by animateFloatAsState(
        targetValue = if (settingState.showSuccess) 1f else 0f,
        animationSpec = SettingsAnimationConstants.Specs.successFadeIn,
        label = "successAlpha"
    )
    
    val errorAlpha by animateFloatAsState(
        targetValue = if (settingState.hasError) 1f else 0f,
        animationSpec = SettingsAnimationConstants.Specs.errorFadeIn,
        label = "errorAlpha"
    )
    
    val shakeOffset by animateFloatAsState(
        targetValue = if (settingState.hasError) 1f else 0f,
        animationSpec = if (settingState.hasError) {
            SettingsAnimationConstants.Specs.errorShake
        } else {
            SettingsAnimationConstants.Specs.noAnimation
        },
        label = "shake"
    )
    
    val switchAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.6f,
        animationSpec = SettingsAnimationConstants.Specs.switchStateTransition,
        label = "switchAlpha"
    )
    
    return remember(loadingAlpha, successScale, successAlpha, errorAlpha, shakeOffset, switchAlpha) {
        SettingsAnimationState(
            loadingAlpha = loadingAlpha,
            successScale = successScale,
            successAlpha = successAlpha,
            errorAlpha = errorAlpha,
            shakeOffset = shakeOffset,
            switchAlpha = switchAlpha
        )
    }
}

private data class SettingsAnimationState(
    val loadingAlpha: Float,
    val successScale: Float,
    val successAlpha: Float,
    val errorAlpha: Float,
    val shakeOffset: Float,
    val switchAlpha: Float
)

@Composable
fun SettingsSwitch(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    settingState: SettingState = SettingState(),
    enabled: Boolean = true,
    onRetry: (() -> Unit)? = null
) {
    val animationState = rememberSettingsAnimationState(settingState, enabled)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = modifier.size(24.dp)
                )
                Spacer(modifier = modifier.width(16.dp))
            }

            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                if (description != null) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = modifier.padding(top = 2.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (AnimationHelpers.isVisibleAlpha(animationState.loadingAlpha)) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(SettingsAnimationConstants.Visual.INDICATOR_SIZE)
                            .padding(end = SettingsAnimationConstants.Visual.INDICATOR_PADDING)
                            .graphicsLayer { alpha = animationState.loadingAlpha },
                        strokeWidth = SettingsAnimationConstants.Visual.INDICATOR_STROKE_WIDTH
                    )
                }
                
                if (AnimationHelpers.isVisibleAlpha(animationState.successAlpha) || 
                    AnimationHelpers.isVisibleScale(animationState.successScale)) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.Green,
                        modifier = Modifier
                            .size(SettingsAnimationConstants.Visual.INDICATOR_SIZE)
                            .padding(end = SettingsAnimationConstants.Visual.INDICATOR_PADDING)
                            .graphicsLayer {
                                alpha = animationState.successAlpha
                                scaleX = animationState.successScale
                                scaleY = animationState.successScale
                            }
                    )
                }
                
                if (AnimationHelpers.isVisibleAlpha(animationState.errorAlpha)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = SettingsAnimationConstants.Visual.INDICATOR_PADDING)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(SettingsAnimationConstants.Visual.INDICATOR_SIZE)
                                .graphicsLayer { alpha = animationState.errorAlpha }
                        )
                        
                        if (settingState.isRetryAvailable && onRetry != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            TextButton(
                                onClick = onRetry,
                                modifier = Modifier.graphicsLayer { alpha = animationState.errorAlpha },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Retry (${settingState.retryCount}/${SettingState.MAX_RETRY_ATTEMPTS})",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else if (settingState.hasError && settingState.hasReachedMaxRetries) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Max retries reached",
                                fontSize = 10.sp,
                                color = Color.Red.copy(alpha = 0.7f),
                                modifier = Modifier.graphicsLayer { alpha = animationState.errorAlpha }
                            )
                        }
                    }
                }
                
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    enabled = enabled,
                    modifier = Modifier.graphicsLayer {
                        translationX = animationState.shakeOffset * SettingsAnimationConstants.Visual.SHAKE_AMPLITUDE.value
                        alpha = animationState.switchAlpha
                    }
                )
            }
        }
        
        if (settingState.hasError && settingState.errorMessage != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = if (icon != null) 40.dp else 0.dp)
                    .graphicsLayer { alpha = animationState.errorAlpha }
            ) {
                Text(
                    text = settingState.errorMessage,
                    fontSize = 12.sp,
                    color = Color.Red
                )
                
                if (settingState.errorType != ErrorType.UNKNOWN) {
                    Text(
                        text = "Error type: ${settingState.errorType.name.lowercase().replace('_', ' ')}",
                        fontSize = 10.sp,
                        color = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}