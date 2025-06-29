package com.choius323.saisai.usecase

import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.AccountToken
import kotlinx.coroutines.flow.Flow

class FetchLoginUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(email: String, password: String): Flow<Result<AccountToken>> =
        accountRepository.login(email, password)
}