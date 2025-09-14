package com.todayilearned.til.ui.ViewModel

import android.app.Application

class DbApp: Application() {
    val db by lazy { TilDatabase.getDatabase(this) }
    val repo by lazy { TilRepository(db) }
    val tilFactory by lazy { TilViewModelFactory(repo) }
}