package com.choius323.saisai.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.choius323.saisai.ui.screen.course.CourseScreen
import com.choius323.saisai.ui.screen.coursedetail.CourseDetailScreen
import com.choius323.saisai.ui.screen.home.HomeScreen
import com.choius323.saisai.ui.screen.login.LoginScreen
import com.choius323.saisai.ui.screen.record.RecordScreen

@Stable
class MainNavController(controller: NavHostController) : SaiNavController(controller)

@Composable
fun MainNavController(
    modifier: Modifier = Modifier,
    navController: MainNavController = rememberSaiNavController(),
) {
    NavHost(
        navController = navController.navController,
        modifier = Modifier,
        startDestination = MainNavItem.Login,
    ) {
        composable<MainNavItem.BottomNavItem.Home> { backStackEntry ->
            HomeScreen(
                modifier = modifier.fillMaxSize(),
                goToDetail = {
                    navController.navigate(MainNavItem.CourseDetail(it))
                }
            )
        }
        composable<MainNavItem.BottomNavItem.Course> { backStackEntry ->
            CourseScreen(modifier.fillMaxSize(), onClickCourse = { courseId ->
                navController.navigate(MainNavItem.CourseDetail(courseId))
            })
        }
        composable<MainNavItem.BottomNavItem.Record> { backStackEntry ->
            RecordScreen(modifier.fillMaxSize()) {
                navController.upPress()
            }
        }
        composable<MainNavItem.CourseDetail> { backStackEntry ->
            val courseDetail = backStackEntry.toRoute<MainNavItem.CourseDetail>()
            CourseDetailScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                goBack = { navController.upPress() }
            )
        }
        composable<MainNavItem.Login> { backStackEntry ->
            LoginScreen(
                modifier = modifier,
                goHome = {
                    navController.navigate(MainNavItem.BottomNavItem.Home) {
                        popUpTo(MainNavItem.Login) { inclusive = true }
                    }
                }
            )
        }
    }
}
