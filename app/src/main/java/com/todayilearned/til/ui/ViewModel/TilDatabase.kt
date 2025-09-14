package com.todayilearned.til.ui.ViewModel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

import kotlin.jvm.Volatile

private const val DB_NAME = "til_database"

@Database(
    entities = [
        TilEntity::class,
        CategoryEntity::class,
        ColorEntity::class,
        TilCategoryCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TilDatabase : RoomDatabase() {
    abstract fun tilDao(): TilDao
    abstract fun colorDao(): ColorDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: TilDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): TilDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    TilDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = inst
                inst
            }
        }
    }
}