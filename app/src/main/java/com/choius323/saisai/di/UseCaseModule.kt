package com.choius323.saisai.di

import com.choius323.saisai.usecase.FetchLoginUseCase
import com.choius323.saisai.usecase.GetAllCoursesUseCase
import com.choius323.saisai.usecase.GetCourseDetailUseCase
import com.choius323.saisai.usecase.GetPopularChallengeUseCase
import com.choius323.saisai.usecase.GetRecentCourseUseCase
import com.choius323.saisai.usecase.GetUserInfoUseCase
import com.choius323.saisai.usecase.PauseRideUseCase
import com.choius323.saisai.usecase.ReissueTokenUseCase
import com.choius323.saisai.usecase.ResumeRideUseCase
import com.choius323.saisai.usecase.SetRecentRideUseCase
import com.choius323.saisai.usecase.ToggleBookmarkCourseUseCase
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
    singleOf(::GetRecentCourseUseCase)
    singleOf(::SetRecentRideUseCase)
    singleOf(::GetUserInfoUseCase)
    singleOf(::ResumeRideUseCase)
    singleOf(::PauseRideUseCase)
    singleOf(::ToggleBookmarkCourseUseCase)
}