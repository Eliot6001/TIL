package com.todayilearned.til.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.capitalize
import com.todayilearned.til.ui.screens.Screen


@Composable
fun BottomNavigationBar(
    items: List<Screen> = Screen.bottomNavItems,
    currentRoute: String,
    onItemSelected: (String) -> Unit,
) {
    println(items + "Received items")
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        onItemSelected(screen.route)
                    }
                },
                icon = { Icon(screen.icon, "open ${screen.label} ") },
                label = { Text(screen.label) }
            )
        }
    }
}