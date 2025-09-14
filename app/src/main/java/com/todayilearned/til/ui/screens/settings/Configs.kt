package com.todayilearned.til.ui.screens.settings


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

object ConfigStore {
    private val FONT_KEY = stringPreferencesKey("font_key")
    private val THEME_KEY = booleanPreferencesKey("pref_dark_theme")

    fun fontKeyFlow(ctx: Context) = ctx.dataStore.data.map {
        it[FONT_KEY] ?: "inter" // default
    }

    suspend fun setFontKey(ctx: Context, key: String) {
        ctx.dataStore.edit { it[FONT_KEY] = key }
    }

    fun isDarkFlow(ctx: Context) = ctx.dataStore.data.map { it[THEME_KEY] ?: false }
    suspend fun setDark(ctx: Context, value: Boolean) = ctx.dataStore.edit { it[THEME_KEY] = value }
}
