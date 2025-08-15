package com.choius323.saisai.usecase

import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.AccountToken
import com.choius323.saisai.ui.model.LoginType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class LoginUseCase(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(token: String, type: LoginType): Flow<Result<AccountToken>> =
        when(type) {
            LoginType.KAKAO -> accountRepository.loginWithKakao(token)
            LoginType.GOOGLE -> accountRepository.loginWithGoogle(token)
        }.onEach { result ->
            result.onSuccess { accountToken ->
                accountRepository.saveToken(accountToken, type.name)
            }
        }
}
