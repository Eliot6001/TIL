package com.todayilearned.til.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField as FoundationBasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.todayilearned.til.ui.screens.create.RemainingCharBadge

@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    maxLines: Int = 1,
    maxCharLength: Int = 200,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocused: ((Boolean) -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    var isFocused by remember { mutableStateOf(false) }
    val charCount = value.length
    val charLimitReached = charCount >= maxCharLength

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = colors.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Box {
            FoundationBasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= maxCharLength) onValueChange(newValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .let {
                        if (maxLines == 1) it.height(56.dp)
                        else it.heightIn(min = 120.dp, max = 200.dp)
                    }
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        onFocused?.invoke(focusState.isFocused)
                    }
                    .border(
                        width = 1.dp,
                        color = when {
                            isFocused -> colors.primary
                            isError || charLimitReached -> colors.error
                            else -> colors.outline.copy(alpha = 0.3f)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                textStyle = textStyle.copy(color = colors.onSurface),
                singleLine = singleLine && maxLines == 1,
                maxLines = maxLines,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = keyboardType
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.onSurface.copy(alpha = 0.5f),
                            style = textStyle.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                    innerTextField()
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            RemainingCharBadge(
                count = charCount,
                limit = maxCharLength,
                isLimitReached = charLimitReached || isError
            )
        }
    }
}
