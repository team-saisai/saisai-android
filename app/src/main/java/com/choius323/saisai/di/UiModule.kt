package com.choius323.saisai.di

import com.choius323.saisai.ui.screen.course.CourseListViewModel
import com.choius323.saisai.ui.screen.coursedetail.CourseDetailViewModel
import com.choius323.saisai.ui.screen.home.HomeViewModel
import com.choius323.saisai.ui.screen.login.LoginViewModel
import com.choius323.saisai.ui.screen.map.MapViewModel
import com.choius323.saisai.ui.screen.record.RecordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CourseDetailViewModel)
    viewModelOf(::CourseListViewModel)
    viewModelOf(::RecordViewModel)
}