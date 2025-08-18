package com.choius323.saisai.ui.model

enum class CourseType(
    val displayName: String,
) {
    challenge("챌린지"), // 챌린지 코스
    general("일반 코스"), // 일반 코스
    ;

    companion object {
        fun getSortList(courseType: CourseType) = when(courseType) {
            challenge -> CourseSort.CHALLENGE_COURSE
            general -> CourseSort.GENERAL_COURSE
        }
    }
}