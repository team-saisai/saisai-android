package com.choius323.saisai.usecase

import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.AccountToken
import kotlinx.coroutines.flow.Flow

class ReissueTokenUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(): Flow<Result<AccountToken>> {
        return accountRepository.reissueToken()
    }
}