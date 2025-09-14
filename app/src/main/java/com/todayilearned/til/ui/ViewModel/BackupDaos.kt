package com.todayilearned.til.ui.ViewModel


import kotlinx.serialization.Serializable
import com.todayilearned.til.ui.ViewModel.TilEntity
import com.todayilearned.til.ui.ViewModel.CategoryEntity
import com.todayilearned.til.ui.ViewModel.ColorEntity
import com.todayilearned.til.ui.ViewModel.TilCategoryCrossRef

@Serializable
data class TilDto(
    val id: String,
    val title: String,
    val content: String,
    val colorId: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class CategoryDto(
    val id: String,
    val name: String
)

@Serializable
data class ColorDto(
    val id: String,
    val value: Long
)

@Serializable
data class CrossRefDto(
    val tilId: String,
    val categoryId: String
)

@Serializable
data class TilBackup(
    val tils: List<TilDto>,
    val categories: List<CategoryDto>,
    val colors: List<ColorDto>,
    val crossRefs: List<CrossRefDto>
)


// Entity -> DTO
fun TilEntity.toDto() = TilDto(id, title, content, colorId, createdAt, updatedAt)
fun CategoryEntity.toDto() = CategoryDto(id, name)
fun ColorEntity.toDto() = ColorDto(id, value)
fun TilCategoryCrossRef.toDto() = CrossRefDto(tilId = tilId, categoryId = categoryId)

// DTO -> Entity
fun TilDto.toEntity() = TilEntity(id = id, title = title, content = content, colorId = colorId, createdAt = createdAt, updatedAt = updatedAt)
fun CategoryDto.toEntity() = CategoryEntity(id = id, name = name)
fun ColorDto.toEntity() = ColorEntity(id = id, value = value)
fun CrossRefDto.toEntity() = TilCategoryCrossRef(tilId = tilId, categoryId = categoryId)
