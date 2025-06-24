package com.choius323.saisai.di

import com.choius323.saisai.usecase.GetAllCoursesUseCase
import com.choius323.saisai.usecase.GetCourseDetailUseCase
import com.choius323.saisai.usecase.GetPopularCourseUseCase
import com.choius323.saisai.usecase.GetRecentCourseUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetRecentCourseUseCase)
    singleOf(::GetAllCoursesUseCase)
    singleOf(::GetCourseDetailUseCase)
    singleOf(::GetPopularCourseUseCase)
}