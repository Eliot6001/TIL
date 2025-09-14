package com.todayilearned.til.ui.screens.detail

import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.todayilearned.til.ui.ViewModel.TilViewModel
import com.todayilearned.til.ui.theme.ComicFont
import java.text.SimpleDateFormat
import java.util.*
import kotlin.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilDetailScreen(
    tilId: String,
    onBack: () -> Unit,
    tilViewModel: TilViewModel,
    useComicFont: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val allTilFull by tilViewModel.allTilFull.collectAsState(initial = emptyList())
    val tilFull = remember(allTilFull, tilId) { allTilFull.find { it.til.id == tilId } }



    Scaffold(
        topBar = {
            TopAppBar (
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("TIL Details", style = typography.titleLarge) },

                modifier= Modifier.padding(0.dp)
            )
        },
        modifier = Modifier.padding(0.dp).then(modifier),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background)
                .padding(12.dp)
        ) {
            when {
                tilFull == null -> {
                    // Loading / Not found
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Loading TIL...", color = colors.onSurfaceVariant)
                    }
                }

                else -> {
                    // Render the TIL
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title with color accent bar on th²e left
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(8.dp)
                                    .height(40.dp)
                                    .background(
                                        color = tilFull.color?.value?.let { Color(it.toInt()) } ?: Color.Transparent
                                    )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = tilFull.til.title,
                                style = typography.headlineMedium,
                                color = colors.primary
                            )
                        }

                        // Content
                        Text(
                            text = tilFull.til.content,
                            style = typography.bodyLarge,
                            color = colors.onSurface
                        )

                        // Categories (horizontal scroll if many)
                        if (tilFull.categories.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(tilFull.categories.size) { idx ->
                                    val cat = tilFull.categories[idx]
                                    AssistChip(
                                        onClick = { /* maybe filter by category later */ },
                                        label = { Text(cat.name) },
                                        colors = AssistChipDefaults.assistChipColors()
                                    )
                                }
                            }
                        }

                        // Created at
                        Text(
                            text = "Created: ${formatTimestamp(tilFull.til.createdAt)}",
                            style = typography.labelSmall,
                            color = colors.outline
                        )
                    }
                }
            }
        }
    }
}


private fun formatTimestamp(time: Long): String {
    return try {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(time))
    } catch (e: Exception) {
        ""
    }
}
