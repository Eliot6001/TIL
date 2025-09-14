package com.todayilearned.til.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressGrid(
    modifier: Modifier = Modifier,
    totalCount: Int,
    last7Days: Int,
    categoriesCount: Int
) {
    Column(modifier = modifier) {
        Text("Today's Values", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProgressCard(label = "Total", currentCount = totalCount, maxCount = maxOf(4, totalCount), modifier = Modifier.weight(1f))
            ProgressCard(label = "This week", currentCount = last7Days, maxCount = maxOf(4, last7Days), modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProgressCard(label = "Categories", currentCount = categoriesCount, maxCount = maxOf(4, categoriesCount), modifier = Modifier.weight(1f))
            ProgressCard(label = "Streak", currentCount = (last7Days.coerceAtMost(7)), maxCount = 7, modifier = Modifier.weight(1f))
        }
    }
}
