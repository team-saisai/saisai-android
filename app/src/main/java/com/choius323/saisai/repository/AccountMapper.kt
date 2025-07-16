package com.choius323.saisai.repository

import com.choius323.saisai.RecentRideProto
import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.account.model.UserBadgeDto
import com.choius323.saisai.data.course.remote.model.UserBadge
import com.choius323.saisai.ui.model.AccountToken
import com.choius323.saisai.ui.model.RecentRide
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun AccountTokenDto.toAccountToken(): AccountToken = AccountToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
)

fun UserBadgeDto.toUserBadge(): UserBadge {
    return UserBadge(
        name = this.badgeName,
        description = this.badgeDescription,
        imageUrl = this.badgeImage,
        acquiredDate = LocalDate.parse(this.acquiredAt, DateTimeFormatter.ISO_LOCAL_DATE)
    )
}

fun RecentRide.toProto(): RecentRideProto {
    val ride = this
    return RecentRideProto.newBuilder().apply {
        rideId = ride.rideId
        courseId = ride.courseId
        lastLatitude = ride.lastLatitude
        lastLongitude = ride.lastLongitude
        lastRideIndex = ride.lastRideIndex
        isSendData = ride.isSendData
        progressRate = ride.progressRate
    }.build()
}

fun RecentRideProto.toDomainModel(): RecentRide {
    return RecentRide(
        rideId = rideId,
        courseId = courseId,
        lastLatitude = lastLatitude,
        lastLongitude = lastLongitude,
        lastRideIndex = lastRideIndex,
        isSendData = isSendData,
        progressRate = progressRate
    )
}