package com.todayilearned.til.ui.screens.viewall

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.todayilearned.til.ui.components.TilCard
import com.todayilearned.til.ui.ViewModel.TilFull
import com.todayilearned.til.ui.ViewModel.TilViewModel
import com.todayilearned.til.ui.components.CategoryChip
import kotlin.math.max

@Composable
fun TilistScreen(
    modifier: Modifier = Modifier,
    viewModel: TilViewModel,
    onTilClick: (TilFull) -> Unit
) {
    var selectedSort by remember { mutableStateOf(SortType.DATE) }
    var showFilters by remember { mutableStateOf(false) }

    // filter selections (sets so duplicates are impossible)
    var selectedCategoryIds by remember { mutableStateOf(setOf<String>()) }
    var selectedColorIds by remember { mutableStateOf(setOf<String>()) }

    val colors = MaterialTheme.colorScheme

    // data from VM
    val tils by viewModel.allTilFull.collectAsState()
    val allCategories by viewModel.categories.collectAsState()
    val allColors by viewModel.colors.collectAsState()

    val categoryUsage = remember(tils) {
        tils.flatMap { it.categories }
            .groupingBy { it.id }
            .eachCount()
    }

    val sortedCategories = allCategories.sortedByDescending { cat ->
        categoryUsage[cat.id] ?: 0
    }
    // filtered + sorted list
    val filteredSorted by remember(tils, selectedSort, selectedCategoryIds, selectedColorIds) {
        derivedStateOf {
            val filtered = tils.filter { t ->
                val catMatch = if (selectedCategoryIds.isEmpty()) true
                else selectedCategoryIds.all { selId -> t.categories.any { it.id == selId } }


                val colorMatch = if (selectedColorIds.isEmpty()) true
                else (t.color?.let { selectedColorIds.contains(it.id) } ?: false)

                catMatch && colorMatch
            }

            when (selectedSort) {
                SortType.COLOR -> filtered.sortedBy { it.color?.value }
                SortType.CATEGORY -> filtered.sortedBy { it.categories.firstOrNull()?.name.orEmpty() }
                SortType.DATE -> filtered.sortedByDescending { it.til.createdAt }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp, 32.dp)
    ) {
        // header row: title + list icon + filter icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your TILs",
                style = MaterialTheme.typography.headlineSmall,
                color = colors.primary
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                SortDropdown(selectedSort) { selectedSort = it }

                IconButton(onClick = { showFilters = !showFilters }) {
                    Icon(Icons.Default.Clear, contentDescription = "Filters")
                }
            }
        }

        // filters panel (collapsible)
        if (showFilters) {
            Spacer(modifier = Modifier.height(12.dp))

            // Categories row
            Text("Categories", style = MaterialTheme.typography.labelMedium, color = colors.onSurface)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                items(sortedCategories) { cat ->

                    val isSelected = selectedCategoryIds.contains(cat.id)
                    CategoryChip(
                        text = cat.name,
                        selected = isSelected,
                        onClick = {
                            selectedCategoryIds = if (isSelected) {
                                selectedCategoryIds - cat.id
                            } else {
                                selectedCategoryIds + cat.id
                            }
                        }
                    )
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            // Colors row
            Text("Colors", style = MaterialTheme.typography.labelMedium, color = colors.onSurface)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    IconButton(onClick = { selectedColorIds = emptySet() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear colors")
                    }
                }
                items(allColors) { colorEntity ->
                    ColorChip(
                        colorId = colorEntity.id,
                        colorValue = colorEntity.value,
                        selected = selectedColorIds.contains(colorEntity.id),
                        onClick = {
                            selectedColorIds = if (selectedColorIds.contains(colorEntity.id))
                                selectedColorIds - colorEntity.id
                            else
                                selectedColorIds + colorEntity.id
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredSorted) { tilFull ->
                // safe color conversion
                val cardColor = safeColor(tilFull.color?.value)
                Row(modifier = Modifier.fillMaxWidth()) {
                    // stripe on left
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .fillMaxHeight()
                            .background(cardColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    TilCard(
                        til = tilFull.til,
                        categories = tilFull.categories,
                        color = cardColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clickable {
                                onTilClick(tilFull)
                            }
                            .padding(end = 4.dp)
                    )
                }
            }
        }
    }
}

/* ---------------- helpers ---------------- */

@Composable
private fun SelectableCategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier
            .height(32.dp)
            .wrapContentWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (selected) colors.primary.copy(alpha = 0.14f) else colors.surfaceVariant,
        contentColor = if (selected) colors.onPrimary else colors.onSurface,
        border = BorderStroke(1.dp, if (selected) colors.primary else colors.outline)
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ColorChip(
    colorId: String,
    colorValue: Long,
    selected: Boolean,
    onClick: () -> Unit
) {
    val display = safeColor(colorValue)
    Surface(
        modifier = Modifier
            .size(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = display,
        border = BorderStroke(2.dp, if (selected) MaterialTheme.colorScheme.primary else Color.Transparent),
        shadowElevation = if (selected) 6.dp else 0.dp
    ) {}
}

private fun safeColor(value: Any?): Color {
    return when (value) {
        is Int -> Color(value)
        is Long -> Color((value and 0xFFFFFFFFL).toInt())
        else -> Color.Transparent
    }
}

@Composable
fun SortDropdown(
    selectedSort: SortType,
    onSortSelected: (SortType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(4.dp)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Sort TILs",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortType.entries.forEach { sortType ->
                DropdownMenuItem(
                    text = { Text(sortType.displayName) },
                    onClick = {
                        onSortSelected(sortType)
                        expanded = false
                    }
                )
            }
        }
    }
}

enum class SortType(val displayName: String) {
    DATE("Date"),
    COLOR("Color"),
    CATEGORY("Category")
}