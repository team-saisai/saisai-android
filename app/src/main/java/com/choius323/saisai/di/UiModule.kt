package com.choius323.saisai.di

import com.choius323.saisai.ui.MainViewModel
import com.choius323.saisai.ui.screen.badge_list.BadgeListViewModel
import com.choius323.saisai.ui.screen.bookmark_courses.BookmarkCoursesViewModel
import com.choius323.saisai.ui.screen.course_detail.CourseDetailViewModel
import com.choius323.saisai.ui.screen.course_list.CourseListViewModel
import com.choius323.saisai.ui.screen.home.HomeViewModel
import com.choius323.saisai.ui.screen.login.LoginViewModel
import com.choius323.saisai.ui.screen.map.MapViewModel
import com.choius323.saisai.ui.screen.my_page.MyPageViewModel
import com.choius323.saisai.ui.screen.nickname_edit.NicknameEditViewModel
import com.choius323.saisai.ui.screen.record.RecordViewModel
import com.choius323.saisai.ui.screen.ride_history.RideHistoryViewModel
import com.choius323.saisai.ui.screen.settings.SettingsViewModel
import com.choius323.saisai.ui.screen.sign_up.SignUpViewModel
import com.choius323.saisai.ui.screen.total_reward.TotalRewardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CourseDetailViewModel)
    viewModelOf(::CourseListViewModel)
    viewModelOf(::RecordViewModel)
    viewModelOf(::BadgeListViewModel)
    viewModelOf(::BookmarkCoursesViewModel)
    viewModelOf(::MyPageViewModel)
    viewModelOf(::NicknameEditViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::RideHistoryViewModel)
    viewModelOf(::TotalRewardViewModel)
    viewModelOf(::SignUpViewModel)
}