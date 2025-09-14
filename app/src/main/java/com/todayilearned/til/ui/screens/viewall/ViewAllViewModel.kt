package com.todayilearned.til.ui.screens.viewall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todayilearned.til.ui.ViewModel.TilDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TilListViewModel(
    private val dao: TilDao
) : ViewModel() {
    val tils = dao.getAllTilFullFlow() // Flow<List<TilFull>>
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
