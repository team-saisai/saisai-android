package com.choius323.saisai.ui.navigation

import androidx.annotation.DrawableRes
import com.choius323.saisai.R
import kotlinx.serialization.Serializable

interface NavItem {
    val name: String?
        get() = this::class.simpleName

    val fullName: String?
        get() = this::class.qualifiedName
}

@Serializable
sealed interface MainNavItem : NavItem {

    @Serializable
    sealed interface BottomNavItem : MainNavItem {

        @get:DrawableRes
        val iconRes: Int

        @get:DrawableRes
        val iconSelectedRes: Int

        val displayName: String

        @Serializable
        data object Home : BottomNavItem {
            override val iconRes = R.drawable.icon_home_mono
            override val iconSelectedRes = R.drawable.icon_home_fill
            override val displayName = "홈"
        }

        @Serializable
        data object Course : BottomNavItem {
            override val iconRes = R.drawable.icon_lightning_mono
            override val iconSelectedRes: Int = R.drawable.icon_lightning_fill
            override val displayName = "코스"
        }

        @Serializable
        data object MyPage : BottomNavItem {
            override val iconRes = R.drawable.icon_user_mono
            override val iconSelectedRes: Int = R.drawable.icon_user_fill
            override val displayName = "마이"
        }

        @Serializable
        data object RideHistory : BottomNavItem {
            override val iconRes = R.drawable.icon_clock_mono
            override val iconSelectedRes: Int = R.drawable.icon_clock_mono
            override val displayName = "기록"
        }
    }

    @Serializable
    data object TotalReward : MainNavItem

    @Serializable
    data class Record(val courseId: Long) : MainNavItem

    @Serializable
    data object Login : MainNavItem

    @Serializable
    data class SignUp(val token: String, val loginType: String) : MainNavItem

    @Serializable
    data class CourseDetail(val courseId: Long) : MainNavItem

    @Serializable
    data object BadgeList : MainNavItem

    @Serializable
    data object NotificationList : MainNavItem

    @Serializable
    data object BookmarkList : MainNavItem

    @Serializable
    data object AppSettings : MainNavItem

    @Serializable
    data class NicknameEdit(val nickname: String) : MainNavItem
}