package com.choius323.saisai.repository

import com.choius323.saisai.data.account.model.AccountTokenDto
import com.choius323.saisai.ui.model.AccountToken

fun AccountTokenDto.toAccountToken(): AccountToken = AccountToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
)