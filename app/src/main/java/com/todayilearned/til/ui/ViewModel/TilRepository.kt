package com.todayilearned.til.ui.ViewModel

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString

class TilRepository(private val db: TilDatabase) {
    val allTilFullFlow: Flow<List<TilFull>> = db.tilDao().getAllTilFullFlow()

    suspend fun insertTil(til: TilEntity) = db.tilDao().insertTil(til)
    suspend fun insertCrossRef(ref: TilCategoryCrossRef) = db.tilDao().insertCrossRef(ref)
    suspend fun clearCrossRefsForTil(tilId: String) = db.tilDao().clearCrossRefsForTil(tilId)

    suspend fun getAllCategories() = db.categoryDao().getAll()
    suspend fun insertCategory(cat: CategoryEntity) = db.categoryDao().insert(cat)
    suspend fun findByName(name: String) = db.categoryDao().findByName(name)

    suspend fun getAllColors() = db.colorDao().getAll()
    suspend fun insertColor(color: ColorEntity) = db.colorDao().insert(color)

    // --- serialization helpers ---
    private val json = Json { prettyPrint = true }

    /**
     * Build JSON backup of current DB state (TIL entities + categories + colors + cross refs)
     */
    suspend fun exportBackupJson(): String = withContext(Dispatchers.IO) {
        // snapshot full items
        val tilFullList = allTilFullFlow.first()
        val tils = tilFullList.map { it.til.toDto() }
        val categories = getAllCategories().map { it.toDto() }
        val colors = getAllColors().map { it.toDto() }

        val crossRefs = tilFullList.flatMap { tf ->
            tf.categories.map { cat -> CrossRefDto(tilId = tf.til.id, categoryId = cat.id) }
        }

        val backup = TilBackup(tils = tils, categories = categories, colors = colors, crossRefs = crossRefs)
        json.encodeToString(TilBackup.serializer(), backup)
    }

    /**
     * Import JSON backup. Smart-merge behavior:
     * - Reuse categories by name (prevents duplicates)
     * - Reuse colors by value (prevents duplicates)
     * - Insert TILs (keeps IDs from backup), then recreate cross refs mapped to final IDs
     *
     * Runs inside a Room transaction.
     */
    suspend fun importBackupJson(jsonText: String) = withContext(Dispatchers.IO) {
        val backup = json.decodeFromString(TilBackup.serializer(), jsonText)

        db.withTransaction {
            // existing lookups
            val existingCategoriesByName = getAllCategories().associateBy { it.name }.toMutableMap() // name -> CategoryEntity
            val existingColorsByValue = getAllColors().associateBy { it.value }.toMutableMap()      // value -> ColorEntity

            // mapping from backup id -> final id
            val categoryIdMap = mutableMapOf<String, String>() // backupCatId -> finalCatId
            val colorIdMap = mutableMapOf<String, String>()    // backupColorId -> finalColorId

            // ensure categories exist (reuse by name)
            for (catDto in backup.categories) {
                val existing = existingCategoriesByName[catDto.name]
                if (existing != null) {
                    categoryIdMap[catDto.id] = existing.id
                } else {
                    val entity = catDto.toEntity()
                    insertCategory(entity)
                    // now entity.id is either given by backup or default; we assume it's the id on the DTO
                    categoryIdMap[catDto.id] = entity.id
                    existingCategoriesByName[entity.name] = entity
                }
            }

            // ensure colors exist (reuse by value)
            for (colorDto in backup.colors) {
                val existing = existingColorsByValue[colorDto.value]
                if (existing != null) {
                    colorIdMap[colorDto.id] = existing.id
                } else {
                    val entity = colorDto.toEntity()
                    insertColor(entity)
                    colorIdMap[colorDto.id] = entity.id
                    existingColorsByValue[entity.value] = entity
                }
            }

            // insert TILs (mapping color ids)
            for (tilDto in backup.tils) {
                // map backup color id -> final color id if we mapped it above
                val mappedColorId = colorIdMap[tilDto.colorId] ?: tilDto.colorId
                val tilEntity = tilDto.toEntity().copy(colorId = mappedColorId)
                insertTil(tilEntity)
                // clear existing cross refs so we can add backup ones
                clearCrossRefsForTil(tilEntity.id)
            }

            // insert cross refs (map category ids)
            for (cr in backup.crossRefs) {
                val mappedCatId = categoryIdMap[cr.categoryId] ?: cr.categoryId
                insertCrossRef(TilCategoryCrossRef(tilId = cr.tilId, categoryId = mappedCatId))
            }
        } // end transaction
    }
}
