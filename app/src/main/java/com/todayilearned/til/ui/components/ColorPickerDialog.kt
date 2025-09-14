package com.todayilearned.til.ui.components

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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ColorPickerDialog(
    visible: Boolean,
    currentColorHex: String,
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    colors: ColorScheme,
    typography: Typography
) {
    if (visible) {
        var selectedColor by remember { mutableStateOf(Color(hexToLong(currentColorHex))) }

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(250)) + scaleIn(initialScale = 0.95f),
                exit = fadeOut(animationSpec = tween(150)) + scaleOut(targetScale = 0.95f)
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = colors.surface,
                    shadowElevation = 12.dp,
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(28.dp)
                            .animateContentSize()
                    ) {
                        Text(
                            text = "Choose Highlight Color",
                            style = typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Select a color to highlight this TIL",
                            style = typography.bodyMedium,
                            color = colors.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        // Current color preview
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(selectedColor)
                                    .border(
                                        width = 2.dp,
                                        color = colors.outline.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )

                            Column {
                                Text(
                                    text = "Selected Color",
                                    style = typography.labelMedium,
                                    color = colors.onSurface.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "#${selectedColor.hex()}",
                                    style = typography.bodyLarge,
                                    color = colors.onSurface
                                )
                            }
                        }

                        // Color palette
                        Text(
                            text = "Choose a color",
                            style = typography.labelLarge,
                            color = colors.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        val colorPalette = listOf(
                            Color(0xFF6750A4), // Primary
                            Color(0xFF03DAC6), // Teal/Accent
                            Color(0xFFB3261E), // Error/Red
                            Color(0xFFE8AA14), // Amber/Yellow
                            Color(0xFF6E9B2E), // Green
                            Color(0xFF007A8A), // Blue
                            Color(0xFF8C5700), // Orange
                            Color(0xFF8B349D)  // Purple
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(bottom = 32.dp)
                        ) {
                            colorPalette.forEach { color ->
                                ColorCircle(
                                    color = color,
                                    isSelected = selectedColor == color,
                                    onClick = { selectedColor = color }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    onColorSelected("#${selectedColor.hex()}")
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Select Color")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderStroke = if (isSelected) {
        BorderStroke(4.dp, MaterialTheme.colorScheme.outline)
    } else {
        BorderStroke(2.dp, Color.Transparent)
    }

    Box(
        modifier = Modifier
            .size(52.dp)
            .border(
                border = borderStroke,
                shape = RoundedCornerShape(26.dp)
            )
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color, RoundedCornerShape(22.dp))
        )
    }
}

// Utility functions
private fun Color.hex(): String {
    return String.format(
        "%02X%02X%02X",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun hexToLong(hex: String): Long {
    return hex.removePrefix("#").toLong(16) or 0xFF000000L
}