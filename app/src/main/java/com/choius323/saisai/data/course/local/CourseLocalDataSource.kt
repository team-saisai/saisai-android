package com.choius323.saisai.data.course.local

import kotlinx.coroutines.flow.Flow

interface CourseLocalDataSource {
    suspend fun getLastShowDateCourseCaution(): Flow<String?>
    suspend fun setLastShowDateCourseCaution(lastShowDate: String)
}

class CourseLocalDataSourceImpl(
    private val courseDataStore: CourseDataStore,
) : CourseLocalDataSource {
    override suspend fun getLastShowDateCourseCaution(): Flow<String?> {
        return courseDataStore.lastShowDate
    }

    override suspend fun setLastShowDateCourseCaution(lastShowDate: String) {
        courseDataStore.saveDate(lastShowDate)
    }
}