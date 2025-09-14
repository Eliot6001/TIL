package com.todayilearned.til.ui.screens.create

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import java.util.*

@Composable
fun RemainingCharBadge(count: Int, limit: Int, isLimitReached: Boolean) {
    val colors = MaterialTheme.colorScheme
    val percentage = count.toFloat() / limit

    val badgeColor by animateColorAsState(
        targetValue = when {
            isLimitReached -> colors.error
            percentage > 0.8 ->  colors.primary.copy(alpha = 0.8f)
            else -> colors.onSurface.copy(alpha = 0.6f)
        },
        animationSpec = tween(300)
    )

    val scale by animateFloatAsState(
        targetValue = if (isLimitReached) 1.1f else 1f,
        animationSpec = keyframes {
            durationMillis = 300
            1f at 0
            1.1f at 150
            1f at 300
        }
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$count/$limit",
            color = badgeColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
