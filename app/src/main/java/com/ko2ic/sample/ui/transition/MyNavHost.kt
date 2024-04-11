package com.ko2ic.sample.ui.transition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ko2ic.sample.ui.compose.post.PostsScreen
import com.ko2ic.sample.ui.compose.profile.ProfileScreen
import com.ko2ic.sample.ui.compose.search.SearchScreen
import com.ko2ic.sample.ui.compose.search.WebViewScreen

const val webViewScreenRouteParameterName = "url"
const val webViewScreenRoute = "webPage/{$webViewScreenRouteParameterName}"

@Composable
fun MyNavHost() {
    val navHostController = rememberNavController()
    val myNavController = remember(key1 = navHostController) {
        MyNavController(navHostController)
    }

    val onNavigateToRoute = myNavController::navigateToBottomBarRoute

    NavHost(
        navController = myNavController.navController,
        startDestination = "top"
    ) {
        navigation(
            route = "top",
            startDestination = BottomTabType.SEARCH.route
        ) {
            composable(BottomTabType.SEARCH.route) { from ->
                SearchScreen(onNavigateToRoute = onNavigateToRoute) { url ->
                    myNavController.navigate(url, from)
                }
            }
            composable(BottomTabType.LIST.route) { from ->
                PostsScreen(onNavigateToRoute = onNavigateToRoute)
            }
            composable(BottomTabType.PROFILE.route) { from ->
                ProfileScreen(onNavigateToRoute = onNavigateToRoute)
            }
        }
        composable(
            webViewScreenRoute,
            arguments = listOf(
                navArgument(webViewScreenRouteParameterName) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            // 勝手にviewModelのSavedStateHandleに設定されるのでここでやる必要なし
//            val url =
//                URLDecoder.decode(backStackEntry.arguments?.getString(webViewScreenRouteParameterName) ?: "", "UTF-8")
            WebViewScreen()
        }
    }
}