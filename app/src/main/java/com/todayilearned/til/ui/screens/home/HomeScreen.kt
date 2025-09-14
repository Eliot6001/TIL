package com.todayilearned.til.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todayilearned.til.ui.ViewModel.TilFull
import com.todayilearned.til.ui.ViewModel.TilViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    tilViewModel: TilViewModel,
    onTilClick: (TilFull) -> Unit = {},
    onThemeToggle: () -> Unit,
    onOpenSettings: () -> Unit,
    isDark: Boolean
) {
    val today = remember {
        try {
            val date = Date()
            val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
            "${dayFormat.format(date)}, ${dateFormat.format(date)}"
        } catch (e: Exception) {
            "Today"
        }
    }

    // collect flows
    val tils by tilViewModel.allTilFull.collectAsState()
    val categories by tilViewModel.categories.collectAsState()
    val colors by tilViewModel.colors.collectAsState()

    // derived values
    val totalCount by remember(tils) { derivedStateOf { tils.size } }
    val last7DaysCount by remember(tils) {
        derivedStateOf {
            val weekAgo = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
            tils.count { it.til.createdAt >= weekAgo }
        }
    }

    val randomHighlight by remember(tils) {
        derivedStateOf { if (tils.isEmpty()) null else tils[Random(System.currentTimeMillis()).nextInt(tils.size)] }
    }

    Scaffold(
        modifier = Modifier.fillMaxHeight().then(modifier),
        topBar = { 
            TopAppBar(title = { 
                Text("Good morning!", style= typography.bodyLarge.copy(
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ))

                              
                              }, 
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        if (isDark) Icon(Icons.Default.LightMode, contentDescription = "Switch to light")
                        else Icon(Icons.Default.DarkMode, contentDescription = "Switch to dark")
                    }

            IconButton(onClick = onOpenSettings) {
                Icon(imageVector = Icons.Default.Settings,
                    contentDescription = "Settings")
            }
        }) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        ) {
            item {
                Text(today, style = typography.bodyLarge, color = Color.Gray)
                Spacer(Modifier.height(16.dp))

                // Progress grid (example uses derived numbers; replace with real sources later)
                ProgressGrid(
                    totalCount = totalCount,
                    last7Days = last7DaysCount,
                    categoriesCount = categories.size
                )

                Spacer(Modifier.height(20.dp))

                // Random highlight (clickable)
                RandomNoteBox(randomHighlight) { onTilClick(it) }

                Spacer(Modifier.height(20.dp))

                Text("Recent", style = typography.titleMedium)
                Spacer(Modifier.height(8.dp))
            }

            // Recent TILs
            items(tils) { tilFull ->
                SimpleNoteItem(
                    title = tilFull.til.title,
                    subtitle = formatAge(tilFull.til.createdAt),
                    color = safeColor(tilFull.color?.value),

                ) {
                    onTilClick(tilFull)
                }
                Spacer(Modifier.height(8.dp))
            }

            item { Spacer(Modifier.height(64.dp)) } // bottom spacing
        }
    }
}


/* small helpers */
private fun formatAge(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    val hours = diff / (1000L * 60 * 60)
    return if (hours < 24) "${hours}h ago" else "${hours / 24}d ago"
}

/** Safely convert stored color (Int or Long) into Compose Color. */
private fun safeColor(value: Any?): Color {
    return when (value) {
        is Int -> Color(value)
        is Long -> Color((value and 0xFFFFFFFF).toInt())
        else -> Color.Transparent
    }
}
