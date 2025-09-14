package com.todayilearned.til.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.Alignment
import com.todayilearned.til.ui.ViewModel.TilRepository
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheetPartial(
    show: Boolean,
    onDismiss: () -> Unit,
    currentFontKey: String,
    onSelectFontKey: (String) -> Unit,
    repo: TilRepository
){
    if (!show) return

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // Don't skip partially expanded for better UX
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // launchers
    val pickFontLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            scope.launch {
                ctx.contentResolver.openInputStream(it)?.use { stream ->
                    replaceCustomFontFile(stream, ctx)
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            scope.launch {
                ctx.contentResolver.openInputStream(it)?.use { stream ->
                    importDataFromStream(stream, ctx, repo)
                }
            }
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            scope.launch {
                exportDataToUri(it, ctx, repo)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        // REMOVE fillMaxHeight() - this was blocking interactions
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp), // Extra bottom padding for navigation bar
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with better styling
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Login section (disabled)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Login (coming soon)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Fonts section with better layout
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Choose Font",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val fonts = listOf(
                        "comic" to "Comic Sans",
                        "inter" to "Inter",
                        "noto" to "Noto Sans",
                        "IBMsans" to "IBM Plex Sans",
                        "custom" to "Custom Font"
                    )

                    fonts.forEach { (key, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectFontKey(key) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (currentFontKey == key),
                                onClick = { onSelectFontKey(key) }
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            if (key == "custom") {
                                OutlinedButton(
                                    onClick = {
                                        pickFontLauncher.launch(arrayOf("font/ttf", "font/*", "*/*"))
                                    },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("Import")
                                }
                            }
                        }
                    }
                }
            }

            // Data section with better styling
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Backup & Restore",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { exportLauncher.launch("til-backup.json") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Export")
                        }
                        Button(
                            onClick = { importLauncher.launch(arrayOf("application/json", "*/*")) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Import")
                        }
                    }
                }
            }
        }
    }
}