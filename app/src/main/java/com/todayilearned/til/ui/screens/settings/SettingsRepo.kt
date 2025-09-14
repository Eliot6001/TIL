package com.todayilearned.til.ui.screens.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "til_prefs")

class SettingsRepository(private val context: Context) {
    companion object {
        private val FONT_KEY = stringPreferencesKey("font_key")
        private val DARK_KEY = booleanPreferencesKey("dark_mode")
        private const val DEFAULT_FONT = "inter"
    }

    val fontKeyFlow = context.dataStore.data.map { prefs ->
        prefs[FONT_KEY] ?: DEFAULT_FONT
    }

    val darkModeFlow = context.dataStore.data.map { prefs ->
        prefs[DARK_KEY] ?: false
    }

    suspend fun setFontKey(key: String) {
        context.dataStore.edit { prefs ->
            prefs[FONT_KEY] = key
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_KEY] = enabled
        }
    }
}