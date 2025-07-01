package com.choius323.saisai.ui.model

data class CourseDetail(
    val courseId: String,
    val courseName: String,
    val summary: String,
    val level: Int,
    val distance: Double, // km 단위로 표시 가정
    val estimatedTime: Double, // 분 단위로 표시 가정
    val sigun: String,
    val imageUrl: String?,
    val inProgressUserCount: Int,
    val completeUserCount: Int,
    val gpxPointList: List<GpxPoint>,
)