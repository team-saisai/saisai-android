package com.choius323.saisai.repository

import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.account.model.TotalRewardDto
import com.choius323.saisai.data.account.model.UserBadgeDetailDto
import com.choius323.saisai.data.account.model.UserBadgeDto
import com.choius323.saisai.data.account.model.UserProfileDto
import com.choius323.saisai.ui.model.AccountToken
import com.choius323.saisai.ui.model.RewardInfo
import com.choius323.saisai.ui.model.TotalReward
import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.model.UserBadgeDetail
import com.choius323.saisai.ui.model.UserProfile
import com.choius323.saisai.util.DateTimeFormat
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun AccountTokenDto.toAccountToken(): AccountToken = AccountToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
)

fun UserBadgeDetailDto.toUserBadgeDetail(): UserBadgeDetail {
    return UserBadgeDetail(
        name = this.badgeName,
        description = this.badgeDescription,
        imageUrl = this.imageUrl,
        acquiredDate = LocalDate.parse(this.acquiredAt, DateTimeFormatter.ISO_LOCAL_DATE)
    )
}

fun List<UserBadgeDto>.toUserBadgeList(): List<UserBadge> = map {
    it.run {
        UserBadge(
            id = id,
            name = name,
            imageUrl = imageUrl,
            description = description,
            condition = condition,
        )
    }
}

fun UserProfileDto.toUserProfile(): UserProfile {
    return UserProfile(
        imageUrl = this.imageUrl,
        nickname = this.nickname,
        email = this.email,
        rideCount = this.rideCount,
        bookmarkCount = this.bookmarkCount,
        reward = this.reward,
        badgeCount = this.badgeCount
    )
}

fun TotalRewardDto.toTotalReward() = TotalReward(
    totalReward = this.totalReward,
    rewardInfoList = rewardInfoList.map {
        RewardInfo(
            reward = it.reward,
            acquiredAt = LocalDate.parse(it.acquiredAt, DateTimeFormat.dateTimeFormat),
            courseName = it.courseName
        )
    }
)