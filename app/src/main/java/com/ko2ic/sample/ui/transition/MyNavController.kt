package com.ko2ic.sample.ui.transition

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController

class MyNavController(
    val navController: NavHostController,
) {

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun navigate(route: String, from: NavBackStackEntry) {
        if (from.getLifecycle().currentState == Lifecycle.State.RESUMED) {
            navController.navigate(route)
        }
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
        return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
    }

    private val NavGraph.startDestination: NavDestination?
        get() = findNode(startDestinationId)
}