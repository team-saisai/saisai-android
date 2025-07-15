package com.choius323.saisai.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    mainNavController: MainNavController,
    modifier: Modifier = Modifier,
) {
    val bottomBarItems = listOf<MainNavItem.BottomNavItem>(
        MainNavItem.BottomNavItem.Home,
        MainNavItem.BottomNavItem.Course,
        MainNavItem.BottomNavItem.MyPage,
    )

    val bottomBarShowItems = listOf(
        MainNavItem.BottomNavItem.Home,
        MainNavItem.BottomNavItem.Course,
        MainNavItem.BottomNavItem.MyPage,
    )

    val navBackStackEntry by mainNavController.navController.currentBackStackEntryAsState()

    // 현재 route가 bottomBarScreens에 포함되는지 확인
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = bottomBarShowItems.any { screen ->
        currentRoute == screen.fullName
    }

    if (shouldShowBottomBar) {
        Row(
            modifier = modifier.padding(vertical = 10.dp, horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (screen in bottomBarItems) {
                val isSelected = currentRoute == screen.fullName
                Column(
                    modifier = Modifier.clickable {
                        // 객체를 직접 전달하여 화면 이동
                        mainNavController.navigate(to = screen, from = navBackStackEntry) {
                            popUpTo(mainNavController.navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(screen.iconRes),
                        contentDescription = screen.name,
                        tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        screen::class.simpleName ?: "",
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}