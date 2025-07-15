package com.choius323.saisai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

@Composable
inline fun <reified T : SaiNavController> rememberSaiNavController(
    navController: NavHostController = rememberNavController(),
): T = remember(navController) {
    when (T::class) {
        MainNavController::class -> MainNavController(navController) as T
        else -> throw IllegalArgumentException("Unsupported SaiNavController type: ${T::class}")
    }
}

@Stable
abstract class SaiNavController(val navController: NavHostController) {
    fun upPress() {
        navController.navigateUp()
    }

    fun <T : NavItem> navigate(
        to: T, from: NavBackStackEntry? = null, navBuilder: NavOptionsBuilder.() -> Unit = {},
    ) {
        navigate(
            to = to,
            from = from ?: navController.currentBackStackEntry,
            navOptions = navOptions(navBuilder)
        )
    }

    fun <T : NavItem> navigate(
        to: T, from: NavBackStackEntry?, navOptions: NavOptions? = null,
    ) {
        val backStackEntry = from ?: navController.currentBackStackEntry
        if (backStackEntry == null || backStackEntry.lifecycleIsResumed()) {
            navController.navigate(to, navOptions)
        }
    }

    fun <T : NavItem> navigateNow(
        to: T, navBuilder: NavOptionsBuilder.() -> Unit = {},
    ) {
        navController.navigate(
            to,
            navOptions = navOptions(navBuilder)
        )
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
