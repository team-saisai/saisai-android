package com.choius323.saisai.usecase

import com.choius323.saisai.repository.AccountRepository

class GetUserInfoUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke() = accountRepository.getUserInfo()
}