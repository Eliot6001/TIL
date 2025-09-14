package com.todayilearned.til.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.todayilearned.til.ui.ViewModel.CategoryEntity
import com.todayilearned.til.ui.screens.create.MAX_CATEGORY_LENGTH
import com.todayilearned.til.ui.screens.create.MIN_CATEGORY_LENGTH

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CategoryDialog(
    visible: Boolean,
    existingCategories: List<CategoryEntity>,
    onAddCategory: (String) -> Unit,
    onDismiss: () -> Unit,
    colors: ColorScheme,
    typography: Typography
) {
    if (!visible) return

    var newCategory by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.98f, animationSpec = tween(220)),
            exit = fadeOut(animationSpec = tween(150)) + scaleOut(targetScale = 0.98f, animationSpec = tween(150))
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colors.surface,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .animateContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(22.dp)
                        .animateContentSize()
                ) {
                    // Header row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Manage Categories",
                            style = typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = colors.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Add a new category to organize your TILs",
                        style = typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Input
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = {
                            newCategory = it
                            error = null
                        },
                        label = { Text("New Category") },
                        placeholder = { Text("e.g. Android Development") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = colors.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        isError = error != null,
                        supportingText = error?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Existing categories section (custom chips)
                    if (existingCategories.isNotEmpty()) {
                        Text(
                            text = "Existing Categories",
                            style = typography.labelLarge,
                            color = colors.onSurface.copy(alpha = 0.85f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            existingCategories.forEach { category ->
                                val isSelected = newCategory.equals(category.name, ignoreCase = true)
                                CategoryChipItem(
                                    text = category.name,
                                    selected = isSelected,
                                    colors = colors,
                                    typography = typography,
                                    onClick = { newCategory = category.name }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                newCategory = ""
                                error = null
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                val trimmed = newCategory.trim()

                                // Validation
                                when {
                                    trimmed.isEmpty() -> error = "Category cannot be empty"
                                    trimmed.length < MIN_CATEGORY_LENGTH -> error = "Category must be at least $MIN_CATEGORY_LENGTH characters"
                                    trimmed.length > MAX_CATEGORY_LENGTH -> error = "Category must be less than $MAX_CATEGORY_LENGTH characters"
                                    existingCategories.any { it.name.equals(trimmed, ignoreCase = true) } -> error = "Category already exists"
                                    else -> {
                                        onAddCategory(trimmed)
                                        newCategory = ""
                                        error = null
                                        onDismiss()
                                    }
                                }
                            },
                            enabled = newCategory.trim().isNotEmpty(),
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Add Category")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Lightweight custom chip UI to avoid FilterChip issues.
 * - Simple Surface + clickable
 * - Uses color scheme to show selected/idle state
 */
@Composable
private fun CategoryChipItem(
    text: String,
    selected: Boolean,
    colors: ColorScheme,
    typography: Typography,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    val containerColor = if (selected) colors.primary.copy(alpha = 0.14f) else colors.surfaceVariant.copy(alpha = 0.06f)
    val border = if (selected) BorderStroke(1.dp, colors.primary.copy(alpha = 0.6f)) else BorderStroke(1.dp, colors.outline.copy(alpha = 0.18f))
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clip(shape)
            .clickable(role = Role.Button, onClick = onClick),
        shape = shape,
        color = containerColor,
        tonalElevation = if (selected) 1.dp else 0.dp,
        shadowElevation = 0.dp,
        border = border
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = typography.labelMedium,
                color = if (selected) colors.primary else colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
