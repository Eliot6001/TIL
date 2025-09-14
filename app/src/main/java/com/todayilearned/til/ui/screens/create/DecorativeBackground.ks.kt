package com.todayilearned.til.ui.screens.create


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*



@Composable
fun DecorativeBackground(colors: ColorScheme) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Subtle radial gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        radius = 1.2f
                    )
                )
        )

        // Abstract shapes
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(220.dp, (-60).dp)
                .blur(40.dp)
                .background(
                    Brush.sweepGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.1f),
                            colors.secondary.copy(alpha = 0.05f),
                            colors.tertiary.copy(alpha = 0.05f),
                            colors.primary.copy(alpha = 0.1f)
                        )
                    ),
                    RoundedCornerShape(90.dp)
                )
        )

        Box(
            modifier = Modifier
                .size(140.dp)
                .offset((-40).dp, 560.dp)
                .blur(30.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.secondary.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    RoundedCornerShape(70.dp)
                )
        )
    }
}
