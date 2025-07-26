package com.tamersarioglu.pairedup.presentation.components.settings

import androidx.compose.animation.core.*
import androidx.compose.ui.unit.dp

/**
 * Animation constants and timing configuration for settings components.
 * Provides consistent animation specifications across all feedback states.
 */
object SettingsAnimationConstants {
    
    // Animation durations (in milliseconds)
    object Durations {
        const val LOADING_FADE_IN = 200
        const val LOADING_FADE_OUT = 200
        const val SUCCESS_SCALE_IN = 150
        const val SUCCESS_FADE_IN = 150
        const val SUCCESS_FADE_OUT = 200
        const val SUCCESS_DISPLAY_DELAY = 1000
        const val ERROR_FADE_IN = 200
        const val ERROR_FADE_OUT = 200
        const val ERROR_SHAKE = 300
        const val SWITCH_STATE_TRANSITION = 300
    }
    
    // Animation specifications for different states
    object Specs {
        // Loading state animations
        val loadingFadeIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.LOADING_FADE_IN,
            easing = FastOutSlowInEasing
        )
        
        val loadingFadeOut: AnimationSpec<Float> = tween(
            durationMillis = Durations.LOADING_FADE_OUT,
            easing = FastOutSlowInEasing
        )
        
        // Success state animations
        val successScaleIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.SUCCESS_SCALE_IN,
            easing = Easings.BOUNCE_OUT
        )
        
        val successFadeIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.SUCCESS_FADE_IN,
            easing = FastOutSlowInEasing
        )
        
        val successFadeOut: AnimationSpec<Float> = tween(
            durationMillis = Durations.SUCCESS_FADE_OUT,
            delayMillis = Durations.SUCCESS_DISPLAY_DELAY,
            easing = FastOutSlowInEasing
        )
        
        // Error state animations
        val errorFadeIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.ERROR_FADE_IN,
            easing = FastOutSlowInEasing
        )
        
        val errorFadeOut: AnimationSpec<Float> = tween(
            durationMillis = Durations.ERROR_FADE_OUT,
            easing = FastOutSlowInEasing
        )
        
        // Shake animation for error state
        val errorShake: AnimationSpec<Float> = keyframes {
            durationMillis = Durations.ERROR_SHAKE
            0f at 0 with LinearEasing
            -5f at 50 with LinearEasing
            5f at 100 with LinearEasing
            -5f at 150 with LinearEasing
            5f at 200 with LinearEasing
            -2f at 250 with LinearEasing
            0f at 300 with LinearEasing
        }
        
        // Switch state transition
        val switchStateTransition: AnimationSpec<Float> = tween(
            durationMillis = Durations.SWITCH_STATE_TRANSITION,
            easing = FastOutSlowInEasing
        )
        
        // No animation (instant)
        val noAnimation: AnimationSpec<Float> = tween(0)
    }
    
    // Visual constants
    object Visual {
        val INDICATOR_SIZE = 16.dp
        val INDICATOR_STROKE_WIDTH = 2.dp
        val INDICATOR_PADDING = 8.dp
        val SHAKE_AMPLITUDE = 5.dp
    }
    
    // Easing functions
    object Easings {
        val SMOOTH_IN_OUT = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
        val BOUNCE_OUT = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)
        val FAST_OUT_SLOW_IN = FastOutSlowInEasing
        val LINEAR = LinearEasing
    }
    
    // Performance optimization constants
    object Performance {
        // Animation frame rate optimization
        const val TARGET_FPS = 60
        const val FRAME_TIME_MS = 1000 / TARGET_FPS
        
        // Memory optimization thresholds
        const val ALPHA_VISIBILITY_THRESHOLD = 0.01f
        const val SCALE_VISIBILITY_THRESHOLD = 0.01f
        
        // Hardware acceleration settings
        const val ENABLE_HARDWARE_ACCELERATION = true
        const val USE_OFFSCREEN_COMPOSITING = true
    }
}


/**
 * 
Performance-optimized animation helper functions
 */
object AnimationHelpers {
    
    /**
     * Checks if an animation value is visible enough to render
     * This helps prevent unnecessary rendering of nearly invisible elements
     */
    fun isVisibleAlpha(alpha: Float): Boolean {
        return alpha > SettingsAnimationConstants.Performance.ALPHA_VISIBILITY_THRESHOLD
    }
    
    /**
     * Checks if a scale animation value is visible enough to render
     */
    fun isVisibleScale(scale: Float): Boolean {
        return scale > SettingsAnimationConstants.Performance.SCALE_VISIBILITY_THRESHOLD
    }
    
    /**
     * Optimizes animation specs for different device capabilities
     * This can be extended to detect device performance and adjust accordingly
     */
    fun getOptimizedAnimationSpec(baseSpec: AnimationSpec<Float>): AnimationSpec<Float> {
        // For now, return the base spec
        // In the future, this could be enhanced to:
        // - Reduce animation duration on low-end devices
        // - Disable complex animations on very low-end devices
        // - Adjust easing functions based on device capabilities
        return baseSpec
    }
}