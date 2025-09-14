package com.todayilearned.til.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import com.todayilearned.til.ui.components.SimpleNoteItem

@Composable
fun RecentNotesList() {
    Column {
        Text("Recent Notes", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        val notes = listOf(
            "Had a meaningful conversation...",
            "Grateful for the sunset walk...",
            "Helped elderly neighbor...",
            "Took time to meditate...",
            "Shared lunch with colleague...",
            "Read inspiring book chapter..."
        )

        notes.forEachIndexed { index, note ->
            SimpleNoteItem(note, "${index + 1} hours ago")
        }
    }
}

