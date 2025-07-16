package com.choius323.saisai.repository

import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.data.account.model.UserBadgeDto
import com.choius323.saisai.data.course.remote.model.UserBadge
import com.choius323.saisai.ui.model.AccountToken
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