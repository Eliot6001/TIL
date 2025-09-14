package com.todayilearned.til


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.todayilearned.til.ui.ViewModel.TilViewModel

import com.todayilearned.til.ui.theme.TILTheme
import com.todayilearned.til.ui.navigation.AppNavigation
import com.todayilearned.til.ui.screens.settings.ConfigStore
import com.todayilearned.til.ui.theme.getDynamicFontFamily
import kotlinx.coroutines.launch


@Composable
fun TILApp(preload: Boolean, viewModel: TilViewModel) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val isDark by ConfigStore.isDarkFlow(ctx).collectAsState(initial = true)

    TILTheme(
        darkTheme = isDark,
        fontScale = 1.0f, // Gotta add a modification later, 0.7f 1.0f 1.3f
        fontFamily = getDynamicFontFamily()
    ) {

        AppNavigation(tilViewModel = viewModel , preload, isDark,onThemeToggle = { scope.launch { ConfigStore.setDark(ctx, !isDark) } })
    }


}
