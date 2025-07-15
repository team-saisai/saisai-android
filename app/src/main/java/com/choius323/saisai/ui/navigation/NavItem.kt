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

        @Serializable
        data object Home : BottomNavItem {
            override val iconRes = R.drawable.icon_home_mono
        }

        @Serializable
        data object Course : BottomNavItem {
            override val iconRes = R.drawable.icon_lightning_mono
        }

        @Serializable
        data object MyPage : BottomNavItem {
            override val iconRes = R.drawable.icon_user_mono
        }
    }

    @Serializable
    data class Record(val courseId: Long) : MainNavItem

    @Serializable
    data object Login : MainNavItem

    @Serializable
    data class CourseDetail(val courseId: Long) : MainNavItem
}