package com.choius323.saisai.di

import com.choius323.saisai.usecase.FetchLoginUseCase
import com.choius323.saisai.usecase.GetAllCoursesUseCase
import com.choius323.saisai.usecase.GetCourseDetailUseCase
import com.choius323.saisai.usecase.GetPopularChallengeUseCase
import com.choius323.saisai.usecase.GetRecentCourseUseCase
import com.choius323.saisai.usecase.ReissueTokenUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetRecentCourseUseCase)
    singleOf(::GetAllCoursesUseCase)
    singleOf(::GetCourseDetailUseCase)
    singleOf(::GetPopularChallengeUseCase)
    singleOf(::FetchLoginUseCase)
    singleOf(::GetCourseDetailUseCase)
    singleOf(::ReissueTokenUseCase)
}