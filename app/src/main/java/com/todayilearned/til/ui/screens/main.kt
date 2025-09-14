package com.todayilearned.til.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.todayilearned.til.ui.navigation.BottomNavigationBar
import com.todayilearned.til.ui.screens.home.HomeScreen
import com.todayilearned.til.ui.screens.create.CreateTilScreen
import com.todayilearned.til.ui.ViewModel.TilViewModel
import com.todayilearned.til.ui.screens.detail.TilDetailScreen
import com.todayilearned.til.ui.screens.settings.ConfigStore
import com.todayilearned.til.ui.screens.settings.SettingsSheetPartial
import com.todayilearned.til.ui.screens.viewall.TilistScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Today", Icons.Filled.Home)
    object Create : Screen("create", "Add", Icons.Filled.Add)
    object TilList : Screen("til_list", "All", Icons.Filled.Book)

    companion object {
        val bottomNavItems: List<Screen>
            get() = listOf(Home, Create, TilList)
    }
}
@Composable
fun MainScreen(
    tilViewModel: TilViewModel,
    isDark: Boolean,
    onThemeToggle: () -> Job,
) {
    NavHostMainScreen(tilViewModel,isDark ,onThemeToggle)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavHostMainScreen(tilViewModel: TilViewModel, isDark: Boolean, onThemeToggle: () -> Job) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var isEnabled by remember { mutableStateOf(true) }
    var lastClickTime = 0L


    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val fontKey by ConfigStore.fontKeyFlow(ctx).collectAsState(initial = "inter")
    var showSettings by remember { mutableStateOf(false) }

    Scaffold(

        bottomBar = {
            BottomNavigationBar(
                items = Screen.bottomNavItems,
                currentRoute = currentRoute ?: Screen.Home.route,
                onItemSelected = { route ->
                    val now = System.currentTimeMillis()
                    if (currentRoute != route && isEnabled && now - lastClickTime > 300L) {
                        lastClickTime = now
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier
            .fillMaxSize()
            .padding(vertical=10.dp, horizontal = 3.dp)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        tilViewModel = tilViewModel, // Pass ViewModel if needed
                        modifier = Modifier.zIndex(if (currentRoute == Screen.Home.route) 5.0f else 0.0f),
                        onOpenSettings = { showSettings = true },
                        isDark = isDark,
                        onThemeToggle = { onThemeToggle() },
                        onTilClick = { tilFull -> navController.navigate("til_detail/${tilFull.til.id}") }
                    )
                }

                composable(Screen.Create.route) {
                    CreateTilScreen(
                        tilFull = null, // No existing TIL for creation
                        viewModel = tilViewModel,
                        onSaveFinished = {
                            navController.popBackStack()
                        },
                        onNavigateBack = { navController.popBackStack() },
                        onAnimationStateChanged = { animating -> isEnabled = !animating },
                        modifier = Modifier
                            .zIndex(if (currentRoute == Screen.Create.route) 5.0f else 0f)

                    )
                }

                composable(Screen.TilList.route) {
                    TilistScreen(
                        onTilClick = { tilFull ->
                            println("Clicked TIL id: ${tilFull.til.id}")
                            navController.navigate("til_detail/${tilFull.til.id}")
                        },
                        modifier = Modifier
                            .zIndex(if (currentRoute == Screen.TilList.route) 5.0f else 0f),
                        viewModel = tilViewModel
                    )
                }

                composable(
                    route = "til_detail/{tilId}",
                    arguments = listOf(navArgument("tilId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val tilId = backStackEntry.arguments?.getString("tilId") ?: ""
                    TilDetailScreen(
                        tilId = tilId,
                        onBack = { navController.popBackStack() },
                        tilViewModel = tilViewModel,
                        modifier = Modifier
                            .padding(vertical = 15.dp)

                    )
                }
            }
            if (showSettings) {
                SettingsSheetPartial(
                    show = showSettings,
                    currentFontKey = fontKey,
                    onSelectFontKey = { key ->
                        scope.launch { ConfigStore.setFontKey(ctx, key) }
                    },
                    repo = tilViewModel.repo,
                    onDismiss = { showSettings = false }
                )
            }
        }
    }
}

