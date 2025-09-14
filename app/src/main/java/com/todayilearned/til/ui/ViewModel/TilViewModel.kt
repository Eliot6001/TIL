package com.todayilearned.til.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Collections.emptyList




class TilViewModel(
    private val repository: TilRepository
) : ViewModel() {

    val repo get() = repository

    val allTilFull: StateFlow<List<TilFull>> =
        repository.allTilFullFlow.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _colors = MutableStateFlow<List<ColorEntity>>(emptyList())
    val colors: StateFlow<List<ColorEntity>> = _colors

    init {
        refreshCategories()
        refreshColors()
    }

    fun refreshCategories() {
        viewModelScope.launch {
            _categories.value = repository.getAllCategories()
        }
    }

    fun refreshColors() {
        viewModelScope.launch {
            _colors.value = repository.getAllColors()
        }
    }

    fun insertTil(
        title: String,
        content: String,
        colorId: String,
        categoryIds: List<String>
    ) {
        viewModelScope.launch {
            val til = TilEntity(title = title, content = content, colorId = colorId)
            repository.insertTil(til)
            categoryIds.forEach { catId ->
                repository.insertCrossRef(TilCategoryCrossRef(tilId = til.id, categoryId = catId))
            }
        }
    }

    suspend fun insertCategorySuspend(name: String): CategoryEntity {
        val existing = repository.findByName(name)
        if (existing != null) return existing
        val newCat = CategoryEntity(name = name)
        repository.insertCategory(newCat)
        _categories.value = repository.getAllCategories()
        return newCat
    }

    fun insertCategory(name: String) {
        viewModelScope.launch { insertCategorySuspend(name) }
    }

    suspend fun insertColorSuspend(value: Long): ColorEntity {
        val color = ColorEntity(value = value)
        repository.insertColor(color)
        _colors.value = repository.getAllColors()
        return color
    }

    fun insertColor(value: Long) {
        viewModelScope.launch { insertColorSuspend(value) }
    }

    suspend fun saveTilSuspend(
        tilId: String?,
        title: String,
        content: String,
        colorId: String,
        categoryIds: List<String>
    ) {
        val id = tilId ?: java.util.UUID.randomUUID().toString()
        val til = TilEntity(id = id, title = title, content = content, colorId = colorId, updatedAt = System.currentTimeMillis())
        repository.insertTil(til)
        repository.clearCrossRefsForTil(id)
        categoryIds.forEach { catId ->
            repository.insertCrossRef(TilCategoryCrossRef(tilId = id, categoryId = catId))
        }
    }
}