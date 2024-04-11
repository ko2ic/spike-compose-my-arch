package com.ko2ic.sample.ui.transition

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun MyNavigationBar(
    tabs: List<BottomTabType>,
    currentRoute: String?,
    onNavigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    NavigationBar {
        tabs.forEach {
            val selected = it == currentSection
            NavigationBarItem(
                icon = { Icon(it.icon, contentDescription = stringResource(id = it.titleResId)) },
                label = { Text(stringResource(id = it.titleResId)) },
                selected = selected,
                onClick = {
                    onNavigateToRoute(it.route)
                }
            )
        }
    }
}