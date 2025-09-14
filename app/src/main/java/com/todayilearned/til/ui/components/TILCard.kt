package com.todayilearned.til.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.todayilearned.til.ui.ViewModel.CategoryEntity
import com.todayilearned.til.ui.ViewModel.TilEntity
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.unit.sp

@Composable
fun TilCard(
    modifier: Modifier = Modifier,
    til: TilEntity,
    categories: List<CategoryEntity>,
    color: Color
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left color strip
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(color)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = til.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Content
                Text(
                    text = til.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Categories
                if (categories.isNotEmpty()) {
                    val scroll = rememberScrollState()
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp) .horizontalScroll(scroll)
                    ) {

                        categories.take(3).forEach { category ->
                            AssistChip(
                                onClick = { /* handle category click if needed */ },
                                label = {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        softWrap = true,

                                    )
                                },
                                shape = CircleShape,
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    labelColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier=Modifier.defaultMinSize(minHeight = 36.dp)
                                    .widthIn(max = 160.dp),

                            )
                        }
                    }
                }
            }
        }
    }
}
