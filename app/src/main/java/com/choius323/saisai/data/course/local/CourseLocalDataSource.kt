package com.choius323.saisai.data.course.local

import com.choius323.saisai.RecentRideProto
import kotlinx.coroutines.flow.Flow

interface CourseLocalDataSource {
    suspend fun getRecentRideCourse(): Flow<RecentRideProto>
    suspend fun setRecentRideCourse(recentRideProto: RecentRideProto)
}

class CourseLocalDataSourceImpl(
    private val courseDataStore: RideDataStore,
) : CourseLocalDataSource {
    override suspend fun getRecentRideCourse(): Flow<RecentRideProto> {
        return courseDataStore.recentRideProto
    }

    override suspend fun setRecentRideCourse(recentRideProto: RecentRideProto) {
        courseDataStore.saveRecentRideProto(recentRideProto)
    }
}