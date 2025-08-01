package com.choius323.saisai.ui.model

enum class CourseSort(val displayName: String) {
    levelAsc("난이도 낮은순"), // 난이도 낮은 순
    levelDesc("난이도 높은순"), // 난이도 높은 순
    participantsDesc("참가자 많은순"), // 참가자 순
    endSoon("종료임박순"), // 종료일 순
}