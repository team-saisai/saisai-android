package com.choius323.saisai.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun BottomNavigationBar(
    mainNavController: MainNavController,
    modifier: Modifier = Modifier,
) {
    val bottomBarItems = listOf(
        MainNavItem.BottomNavItem.Home,
        MainNavItem.BottomNavItem.Course,
        MainNavItem.BottomNavItem.RideHistory,
        MainNavItem.BottomNavItem.MyPage,
    )

    val bottomBarShowItems = listOf(
        MainNavItem.BottomNavItem.Home,
        MainNavItem.BottomNavItem.Course,
        MainNavItem.BottomNavItem.MyPage,
    )

    val navBackStackEntry by mainNavController.navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = bottomBarShowItems.any { screen ->
        currentRoute == screen.fullName
    }

    BottomNavigationBarContent(
        shouldShowBottomBar,
        bottomBarItems,
        currentRoute,
        modifier,
    ) { screen ->
        mainNavController.navigate(to = screen, from = navBackStackEntry) {
            popUpTo(mainNavController.navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
private fun BottomNavigationBarContent(
    shouldShowBottomBar: Boolean,
    bottomBarItems: List<MainNavItem.BottomNavItem>,
    currentRoute: String?,
    modifier: Modifier = Modifier,
    navigate: (MainNavItem.BottomNavItem) -> Unit
) {
    if (shouldShowBottomBar) {
        Box(
            modifier
                .background(Color(0xFF292C2F)),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .width(390.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                for (screen in bottomBarItems) {
                    val isSelected = currentRoute == screen.fullName
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { navigate(screen) }
                            .padding(top = 10.dp, bottom = 16.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(if (isSelected) screen.iconSelectedRes else screen.iconRes),
                            contentDescription = screen.name,
                            tint = if (isSelected) SaiColor.White else SaiColor.Gray50
                        )
                        SaiText(
                            screen.displayName,
                            color = if (isSelected) SaiColor.White else SaiColor.Gray50
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    val bottomBarItems = listOf(
        MainNavItem.BottomNavItem.Home,
        MainNavItem.BottomNavItem.Course,
        MainNavItem.BottomNavItem.RideHistory,
        MainNavItem.BottomNavItem.MyPage,
    )

    SaiTheme {
        BottomNavigationBarContent(
            shouldShowBottomBar = true,
            modifier = Modifier,
            bottomBarItems = bottomBarItems,
            currentRoute = MainNavItem.BottomNavItem.Home.fullName,
        ) {}
    }
}