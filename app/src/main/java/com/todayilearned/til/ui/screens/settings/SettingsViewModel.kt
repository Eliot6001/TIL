package com.todayilearned.til.ui.screens.settings


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo: SettingsRepository) : ViewModel() {
    val fontKey = repo.fontKeyFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "inter")
    val darkMode = repo.darkModeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun selectFont(key: String) {
        viewModelScope.launch {
            repo.setFontKey(key)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            repo.setDarkMode(!(darkMode.value))
        }
    }
}
