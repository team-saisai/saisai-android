package com.choius323.saisai.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.choius323.saisai.ui.component.TopAppBarHeight
import com.choius323.saisai.ui.screen.badge_list.BadgeListScreen
import com.choius323.saisai.ui.screen.bookmark_courses.BookmarkCoursesScreen
import com.choius323.saisai.ui.screen.course_detail.CourseDetailScreen
import com.choius323.saisai.ui.screen.course_list.CourseListScreen
import com.choius323.saisai.ui.screen.home.HomeScreen
import com.choius323.saisai.ui.screen.login.LoginScreen
import com.choius323.saisai.ui.screen.my_page.MyPageScreen
import com.choius323.saisai.ui.screen.nickname_edit.NicknameEditScreen
import com.choius323.saisai.ui.screen.notification_list.NotificationListScreen
import com.choius323.saisai.ui.screen.record.RecordScreen
import com.choius323.saisai.ui.screen.ride_history.RideHistoryScreen
import com.choius323.saisai.ui.screen.settings.SettingsScreen
import com.choius323.saisai.ui.screen.sign_up.SignUpScreen
import com.choius323.saisai.ui.screen.total_reward.TotalRewardScreen

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
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Starts off-screen to the right
                animationSpec = tween(durationMillis = 500)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // Moves off-screen to the left
                animationSpec = tween(durationMillis = 500)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // Reverse direction for back navigation
                animationSpec = tween(durationMillis = 500)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // Reverse exit direction
                animationSpec = tween(durationMillis = 500)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        composable<MainNavItem.BottomNavItem.Home> { backStackEntry ->
            HomeScreen(
                modifier = modifier.fillMaxSize(),
                goNotificationList = { navController.navigate(MainNavItem.NotificationList) },
                goToDetail = {
                    navController.navigate(MainNavItem.CourseDetail(it))
                }
            )
        }
        composable<MainNavItem.BottomNavItem.Course> { backStackEntry ->
            CourseListScreen(modifier.fillMaxSize(), onClickCourse = { courseId ->
                navController.navigate(MainNavItem.CourseDetail(courseId))
            })
        }
        composable<MainNavItem.Record> { backStackEntry ->
            RecordScreen(
                Modifier.fillMaxSize(),
                goHome = {
                    navController.navigate(MainNavItem.BottomNavItem.Home) {
                        popUpTo(MainNavItem.BottomNavItem.Home) { inclusive = true }
                    }
                },
                goCourseDetail = {
                    navController.navigate(MainNavItem.CourseDetail(it)) {
                        popUpTo<MainNavItem.CourseDetail> { inclusive = true }
                    }
                },
                onBack = { navController.upPress() }
            )
        }
        composable<MainNavItem.BottomNavItem.MyPage> { backStackEntry ->
            MyPageScreen(
                modifier.fillMaxSize(),
                goProfile = {},
                goBadgeAchievement = { navController.navigate(MainNavItem.BadgeList) },
                goSetting = { navController.navigate(MainNavItem.AppSettings) },
                goNotificationList = { navController.navigate(MainNavItem.NotificationList) },
                goBookmarkCourses = { navController.navigate(MainNavItem.BookmarkList) },
                goRideHistoryCourse = { navController.navigate(MainNavItem.BottomNavItem.RideHistory) },
                goRewardHistory = { navController.navigate(MainNavItem.TotalReward) },
                goTermsOfService = {},
                goNicknameEdit = { navController.navigate(MainNavItem.NicknameEdit(it)) },
            )
        }
        composable<MainNavItem.CourseDetail> { backStackEntry ->
            CourseDetailScreen(
                modifier = Modifier.fillMaxSize(),
                goRecordScreen = { navController.navigate(MainNavItem.Record(it)) },
                goBack = { navController.upPress() }
            )
        }
        composable<MainNavItem.Login> { backStackEntry ->
            LoginScreen(
                modifier = Modifier,
                goSignUp = { token, loginType ->
                    navController.navigate(MainNavItem.SignUp(token, loginType))
                },
                goHome = {
                    navController.navigate(MainNavItem.BottomNavItem.Home) {
                        popUpTo(MainNavItem.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<MainNavItem.SignUp> { backStackEntry ->
            SignUpScreen(
                modifier = modifier,
                goHome = {
                    navController.navigate(MainNavItem.BottomNavItem.Home) {
                        popUpTo(MainNavItem.Login) { inclusive = true }
                    }
                },
                goBack = { navController.upPress() }
            )
        }
        composable<MainNavItem.BadgeList> { backStackEntry ->
            BadgeListScreen(
                modifier = modifier.fillMaxSize(),
                goBack = { navController.upPress() }
            )
        }
        composable<MainNavItem.NotificationList> { backStackEntry ->
            NotificationListScreen(
                modifier = modifier.fillMaxSize(),
                goBack = { navController.upPress() }
            )
        }
        composable<MainNavItem.BookmarkList> { backStackEntry ->
            BookmarkCoursesScreen(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = TopAppBarHeight)
                    .fillMaxSize(),
                goCourseDetail = { navController.navigate(MainNavItem.CourseDetail(it)) },
                goCourseList = {
                    navController.navigate(MainNavItem.BottomNavItem.Course) {
                        popUpTo(MainNavItem.BottomNavItem.MyPage)
                    }
                },
                goBack = { navController.upPress() },
            )
        }
        composable<MainNavItem.AppSettings> { backStackEntry ->
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                goBack = { navController.upPress() },
                goLogin = {
                    navController.navigate(MainNavItem.Login) {
                        popUpTo(MainNavItem.Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<MainNavItem.NicknameEdit> { backStackEntry ->
            NicknameEditScreen(
                modifier = modifier.fillMaxSize(),
                goBack = {
                    navController.navigate(MainNavItem.BottomNavItem.MyPage) {
                        popUpTo(MainNavItem.BottomNavItem.MyPage) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<MainNavItem.BottomNavItem.RideHistory> { backStackEntry ->
            RideHistoryScreen(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = TopAppBarHeight)
                    .fillMaxSize(),
                goCourseDetail = { navController.navigate(MainNavItem.CourseDetail(it)) },
                goCourseList = {
                    navController.navigate(MainNavItem.BottomNavItem.Course) {
                        popUpTo(MainNavItem.BottomNavItem.MyPage)
                    }
                },
                goBack = { navController.upPress() },
            )
        }
        composable<MainNavItem.TotalReward> { backStackEntry ->
            TotalRewardScreen(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = TopAppBarHeight)
                    .fillMaxSize(),
                goToCourseList = {
                    navController.navigate(MainNavItem.BottomNavItem.Course) {
                        popUpTo(MainNavItem.BottomNavItem.MyPage) {
                            inclusive = true
                        }
                    }
                },
                goBack = { navController.upPress() }
            )
        }
    }
}