package com.todayilearned.til.ui.ViewModel


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction
import java.util.UUID

@Entity
data class TilEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val colorId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity
data class CategoryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String
)

@Entity(primaryKeys = ["tilId", "categoryId"])
data class TilCategoryCrossRef(
    val tilId: String,
    val categoryId: String
)

@Entity
data class ColorEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val value: Long // store ARGB as Long
)

/** Relation projections **/
data class TilFull(
    @Embedded val til: TilEntity,

    // Color relation
    @Relation(
        parentColumn = "colorId",
        entityColumn = "id"
    )
    val color: ColorEntity? = null,

    // Categories via junction: returns list of categories attached to this til
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TilCategoryCrossRef::class,
            parentColumn = "tilId",
            entityColumn = "categoryId"
        ),
        entity = CategoryEntity::class
    )
    val categories: List<CategoryEntity> = emptyList()
)

data class CategoryWithTils(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TilCategoryCrossRef::class,
            parentColumn = "categoryId",
            entityColumn = "tilId"
        ),
        entity = TilEntity::class
    )
    val tils: List<TilEntity> = emptyList()
)