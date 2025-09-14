package com.todayilearned.til.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun CategoryChip(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onRemove: (() -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    val bg = if (selected) colors.primary.copy(alpha = 0.14f) else colors.surfaceVariant
    val contentColor = if (selected) colors.onPrimary else colors.onSurface

    Surface(
        modifier = Modifier
            .height(32.dp)
            .defaultMinSize(minHeight = 32.dp)
            .wrapContentWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = bg,
        contentColor = contentColor,
        border = BorderStroke(1.dp, if (selected) colors.primary else colors.outline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = colors.onSurface)
            if (onRemove != null) {
                Spacer(Modifier.width(6.dp))
                IconButton(onClick = onRemove, modifier = Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = contentColor)
                }
            }
        }
    }
}
