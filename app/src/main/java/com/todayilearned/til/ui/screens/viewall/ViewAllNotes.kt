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
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text


import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.todayilearned.til.ui.components.TilCard
import com.todayilearned.til.ui.ViewModel.TilFull
import com.todayilearned.til.ui.ViewModel.TilViewModel
import com.todayilearned.til.ui.components.CategoryChip


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
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filters")
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
