package com.todayilearned.til

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.todayilearned.til.ui.components.ConfigReader
import com.todayilearned.til.ui.ViewModel.TilViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preload = ConfigReader.shouldPreloadScreens(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val factory = (application as com.todayilearned.til.ui.ViewModel.DbApp).tilFactory
            val vm: TilViewModel = viewModel(factory = factory)

            TILApp(
                preload,
                viewModel = vm,
                )
        }
    }
}
