package com.tamersarioglu.pairedup.domain.model

/**
 * Enum representing different types of settings that can be managed individually.
 * Used to identify which specific setting is being saved or has encountered an error.
 */
enum class SettingType {
    DARK_THEME,
    TIMER_ENABLED,
    SOUND_ENABLED,
    VIBRATION_ENABLED
}