package com.todayilearned.til.ui.screens.create

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todayilearned.til.ui.ViewModel.TilViewModel
import com.todayilearned.til.ui.components.BasicTextField as InputField
import com.todayilearned.til.ui.components.CategoryChip
import com.todayilearned.til.ui.components.CategoryDialog
import com.todayilearned.til.ui.components.ColorPickerDialog
import com.todayilearned.til.ui.ViewModel.TilFull
import com.todayilearned.til.ui.components.hexToLong
import kotlinx.coroutines.launch

const val MAX_TITLE_LENGTH = 100
const val MAX_CONTENT_LENGTH = 200
const val MIN_CATEGORY_LENGTH = 2
const val MAX_CATEGORY_LENGTH = 20

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateTilScreen(
    tilFull: TilFull? = null,
    viewModel: TilViewModel,
    onSaveFinished: () -> Unit,
    onNavigateBack: () -> Unit,
    onAnimationStateChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val scope = rememberCoroutineScope()

    // Collect flows with initial values
    val allCategories by viewModel.categories.collectAsState(initial = emptyList())
    val allColors by viewModel.colors.collectAsState(initial = emptyList())

    // Form state
    var title by remember { mutableStateOf(tilFull?.til?.title ?: "") }
    var content by remember { mutableStateOf(tilFull?.til?.content ?: "") }
    var selectedCategoryIds by remember {
        mutableStateOf(tilFull?.categories?.map { it.id } ?: emptyList<String>())
    }
    var selectedColorId by remember {
        mutableStateOf<String?>(tilFull?.color?.id ?: allColors.firstOrNull()?.id)
    }

    // Dialog states
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showColorPickerDialog by remember { mutableStateOf(false) }

    // UI states
    var isLoading by remember { mutableStateOf(false) }
    val visible = remember { mutableStateOf(true) }
    val scroll = rememberScrollState()

    // Computed properties
    val titleLimitReached = title.length >= MAX_TITLE_LENGTH
    val contentLimitReached = content.length >= MAX_CONTENT_LENGTH
    val isSaveEnabled = (title.isNotBlank() || content.isNotBlank()) && selectedColorId != null

    // Effects
    LaunchedEffect(Unit) {
        onAnimationStateChanged(true)
        onAnimationStateChanged(false)
    }

    LaunchedEffect(allColors) {
        if (selectedColorId == null && allColors.isNotEmpty()) {
            selectedColorId = allColors.first().id
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()   ,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.onSurface
                        )
                    }
                },
                title = {
                    Text(
                        text = "Today I learned..",
                        style = typography.titleLarge,
                        color = colors.onSurface
                    )
                }
            )
        }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom=100.dp)
                .background(colors.background)
                .pointerInput(Unit) {
                    detectTapGestures {
                        showCategoryDialog = false
                        showColorPickerDialog = false
                    }
                } .verticalScroll(rememberScrollState())

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()


                    .padding(24.dp) // Content padding only, not the whole screen
            ){

            // Title input
            InputField(
                value = title,
                onValueChange = { newValue ->
                    if (newValue.length <= MAX_TITLE_LENGTH) {
                        title = newValue
                    }
                },
                placeholder = "What did you learn today?",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = if (titleLimitReached) colors.error else colors.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                textStyle = typography.headlineMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface
                )
            )

            Text(
                text = "${title.length}/$MAX_TITLE_LENGTH",
                modifier = Modifier.align(Alignment.End),
                color = colors.outline
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Content input
            InputField(
                value = content,
                onValueChange = { newValue ->
                    if (newValue.length <= MAX_CONTENT_LENGTH) {
                        content = newValue
                    }
                },
                placeholder = "Write something cool...",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(120.dp, 250.dp),
                textStyle = typography.bodyLarge.copy(
                    lineHeight = 24.sp,
                    color = colors.onSurface
                ),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Categories section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categories",
                    style = typography.labelLarge,
                    color = colors.onSurface.copy(alpha = 0.8f)
                )
                IconButton(onClick = { showCategoryDialog = true }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add category",
                        tint = colors.primary
                    )
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (allCategories.isEmpty()) {
                    Text(
                        text = "No categories",
                        color = colors.onSurface.copy(alpha = 0.5f)
                    )
                } else {
                    allCategories.forEach { category ->
                        val isSelected = selectedCategoryIds.contains(category.id)
                        CategoryChip(
                            text = category.name,
                            selected = isSelected,
                            onClick = {
                                selectedCategoryIds = if (isSelected) {
                                    selectedCategoryIds - category.id
                                } else {
                                    selectedCategoryIds + category.id
                                }
                            },
                            onRemove = {
                                selectedCategoryIds = selectedCategoryIds - category.id
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Color selection
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showColorPickerDialog = true }
                    .background(colors.primary.copy(alpha = 0.05f))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val colorEntity = allColors.find { it.id == selectedColorId }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colorEntity?.let { Color(it.value) } ?: Color.Gray)
                            .border(
                                width = 2.dp,
                                color = colors.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Current highlight",
                        style = typography.bodyLarge,
                        color = colors.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.Create,
                        contentDescription = "Edit color",
                        tint = colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save button
            Button(
                onClick = {
                    val colorId = selectedColorId
                    if (isSaveEnabled && colorId != null && !isLoading) {
                        isLoading = true
                        scope.launch {
                            try {
                                viewModel.saveTilSuspend(
                                    tilId = tilFull?.til?.id,
                                    title = title,
                                    content = content,
                                    colorId = colorId,
                                    categoryIds = selectedCategoryIds
                                )
                                onSaveFinished()
                            } catch (e: Exception) {
                                // Handle error appropriately
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = isSaveEnabled && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save TIL")
                }
            }
        }}

        // Category dialog
        if (showCategoryDialog) {
            CategoryDialog(
                visible = showCategoryDialog,
                existingCategories = allCategories,
                onAddCategory = { newCategoryName ->
                    scope.launch {
                        try {
                            val created = viewModel.insertCategorySuspend(newCategoryName)
                            selectedCategoryIds = selectedCategoryIds + created.id
                            showCategoryDialog = false
                        } catch (e: Exception) {
                            // Handle error
                            showCategoryDialog = false
                        }
                    }
                },
                onDismiss = { showCategoryDialog = false },
                colors = colors,
                typography = typography
            )
        }

        // Color picker dialog
        if (showColorPickerDialog) {
            ColorPickerDialog(
                visible = showColorPickerDialog,
                currentColorHex = allColors.find { it.id == selectedColorId }
                    ?.let { colorToHex(it.value) } ?: "#AFAFAF",
                onColorSelected = { hex ->
                    scope.launch {
                        try {
                            val newVal = hexToLong(hex)
                            val createdColor = viewModel.insertColorSuspend(newVal)
                            selectedColorId = createdColor.id
                            viewModel.refreshColors()
                            showColorPickerDialog = false
                        } catch (e: Exception) {
                            // Handle error
                            showColorPickerDialog = false
                        }
                    }
                },
                onDismiss = { showColorPickerDialog = false },
                colors = colors,
                typography = typography
            )
        }
    }
}

private fun hexToLong(hex: String): Long {
    val clean = hex.removePrefix("#").trim()
    val parsed = clean.toLong(16)
    return when (clean.length) {
        6 -> (0xFF000000L or (parsed and 0xFFFFFFL))
        8 -> (parsed and 0xFFFFFFFFL)
        else -> (0xFF000000L or (parsed and 0xFFFFFFL)) // fallback
    }
}

private fun colorToHex(value: Any?): String {
    return when (value) {
        is Int -> "%06X".format(value and 0xFFFFFF).let { "#$it" }
        is Long -> {
            val rgb = (value and 0xFFFFFFFFL).toInt() and 0xFFFFFF
            "#%06X".format(rgb)
        }
        else -> "#AFAFAF"
    }
}