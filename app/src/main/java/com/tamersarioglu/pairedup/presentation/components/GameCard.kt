package com.tamersarioglu.pairedup.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamersarioglu.pairedup.domain.model.Card
import com.tamersarioglu.pairedup.presentation.ui.theme.CardBackground
import com.tamersarioglu.pairedup.presentation.ui.theme.CardFlipped
import com.tamersarioglu.pairedup.presentation.ui.theme.CardMatched
import com.tamersarioglu.pairedup.utils.Constants

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    card: Card,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {
    var rotationState by remember { mutableFloatStateOf(0f) }

    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = Constants.CARD_FLIP_DURATION,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { rotationState = it },
        label = "card_rotation"
    )

    val cardColor = when {
        card.isMatched -> CardMatched
        card.isFlipped -> CardFlipped
        else -> CardBackground
    }

    val animatedColor by animateColorAsState(
        targetValue = cardColor,
        animationSpec = tween(300),
        label = "card_color"
    )

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(Constants.CARD_CORNER_RADIUS.dp))
            .clickable(enabled = isEnabled && !card.isFlipped && !card.isMatched) {
                onClick()
            }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (card.isFlipped || card.isMatched) 2.dp else Constants.CARD_ELEVATION.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = animatedColor
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            if (rotation > 90f) {
                Text(
                    text = card.number.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (card.isMatched) Color.White else Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = modifier.graphicsLayer {
                        rotationY = 180f
                    }
                )
            } else {
                CardBackPattern(
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CardBackPattern(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(Constants.CARD_CORNER_RADIUS.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}