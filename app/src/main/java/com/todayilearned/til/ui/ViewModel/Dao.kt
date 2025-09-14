package com.todayilearned.til.ui.ViewModel


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity ORDER BY name ASC")
    suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)
}

@Dao
interface ColorDao {
    @Query("SELECT * FROM ColorEntity ORDER BY id")
    suspend fun getAll(): List<ColorEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(color: ColorEntity)

    @Delete
    suspend fun delete(color: ColorEntity)
}

@Dao
interface TilDao {
    // Reactive stream of full TIL records (with color + categories)
    @Transaction
    @Query("SELECT * FROM TilEntity ORDER BY updatedAt DESC")
    fun getAllTilFullFlow(): Flow<List<TilFull>>

    @Transaction
    @Query("SELECT * FROM TilEntity WHERE id = :tilId LIMIT 1")
    fun getTilFullByIdFlow(tilId: String): Flow<TilFull?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTil(til: TilEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(ref: TilCategoryCrossRef)

    @Query("DELETE FROM TilCategoryCrossRef WHERE tilId = :tilId")
    suspend fun clearCrossRefsForTil(tilId: String)

    @Delete
    suspend fun deleteTil(til: TilEntity)
}