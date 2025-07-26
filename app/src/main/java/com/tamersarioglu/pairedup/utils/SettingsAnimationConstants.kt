package com.tamersarioglu.pairedup.utils

import androidx.compose.animation.core.*
import androidx.compose.ui.unit.dp

object SettingsAnimationConstants {
    
    object Durations {
        const val LOADING_FADE_IN = 200
        const val SUCCESS_SCALE_IN = 150
        const val SUCCESS_FADE_IN = 150
        const val ERROR_FADE_IN = 200
        const val ERROR_SHAKE = 300
        const val SWITCH_STATE_TRANSITION = 300
    }
    
    object Specs {
        val loadingFadeIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.LOADING_FADE_IN,
            easing = FastOutSlowInEasing
        )
        
        val successScaleIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.SUCCESS_SCALE_IN,
            easing = Easings.BOUNCE_OUT
        )
        
        val successFadeIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.SUCCESS_FADE_IN,
            easing = FastOutSlowInEasing
        )
        
        val errorFadeIn: AnimationSpec<Float> = tween(
            durationMillis = Durations.ERROR_FADE_IN,
            easing = FastOutSlowInEasing
        )
        
        val errorShake: AnimationSpec<Float> = keyframes {
            durationMillis = Durations.ERROR_SHAKE
            0f at 0 using LinearEasing
            -5f at 50 using LinearEasing
            5f at 100 using LinearEasing
            -5f at 150 using LinearEasing
            5f at 200 using LinearEasing
            -2f at 250 using LinearEasing
            0f at 300 using LinearEasing
        }
        
        val switchStateTransition: AnimationSpec<Float> = tween(
            durationMillis = Durations.SWITCH_STATE_TRANSITION,
            easing = FastOutSlowInEasing
        )
        
        val noAnimation: AnimationSpec<Float> = tween(0)
    }
    
    object Visual {
        val INDICATOR_SIZE = 16.dp
        val INDICATOR_STROKE_WIDTH = 2.dp
        val INDICATOR_PADDING = 8.dp
        val SHAKE_AMPLITUDE = 5.dp
    }
    
    object Easings {
        val SMOOTH_IN_OUT = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
        val BOUNCE_OUT = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)
        val FAST_OUT_SLOW_IN = FastOutSlowInEasing
        val LINEAR = LinearEasing
    }
    
    object Performance {
        const val TARGET_FPS = 60
        const val ALPHA_VISIBILITY_THRESHOLD = 0.01f
        const val SCALE_VISIBILITY_THRESHOLD = 0.01f

    }
}

object AnimationHelpers {

    fun isVisibleAlpha(alpha: Float): Boolean {
        return alpha > SettingsAnimationConstants.Performance.ALPHA_VISIBILITY_THRESHOLD
    }

    fun isVisibleScale(scale: Float): Boolean {
        return scale > SettingsAnimationConstants.Performance.SCALE_VISIBILITY_THRESHOLD
    }

}