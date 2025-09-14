package com.todayilearned.til.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.todayilearned.til.ui.ViewModel.TilFull

@Composable
fun RandomNoteBox(random: TilFull?, onClick: (TilFull) -> Unit = {}) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = random != null) { random?.let(onClick) }
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            if (random == null) {
                Text("No notes yet. Create your first TIL!", style = MaterialTheme.typography.bodyLarge)
            } else {
                Column {
                    Text("Random pick", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(6.dp))
                    Text(random.til.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
                    Spacer(Modifier.height(4.dp))
                    Text(random.til.content, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
