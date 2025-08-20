package com.choius323.saisai.usecase

import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.LoginType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class LoginUseCase(
    private val accountRepository: AccountRepository,
    private val accessServiceUseCase: AccessServiceUseCase,
) {
    /**
     * 소셜 로그인을 시도하고, 사용자가 신규인지 여부를 반환합니다.
     * 신규 유저가 아닐 경우 (isNewUser = false), 내부적으로 로그인 및 토큰 저장 로직을 수행합니다.
     *
     * @param token 소셜 로그인 토큰 (Google ID 토큰 또는 Kakao Access Token)
     * @param type 로그인 유형 (GOOGLE 또는 KAKAO)
     * @return Flow<Result<Boolean>>: true이면 신규 유저, false이면 기존 유저. Result는 작업 성공/실패를 나타냅니다.
     */
    suspend operator fun invoke(token: String, type: LoginType): Flow<Result<Boolean>> {
        val isNewUserFlow: Flow<Result<Boolean>> = when (type) {
            LoginType.GOOGLE -> accountRepository.isNewUserGoogle(token)
            LoginType.KAKAO -> accountRepository.isNewUserKakao(token)
        }

        return isNewUserFlow.transform { isNewUserResult ->
            // isNewUserResult가 성공이고, 그 값이 false (기존 유저)인 경우에만 로그인 로직 실행
            if (isNewUserResult.isSuccess && !isNewUserResult.getOrThrow()) {
                // 기존 유저인 경우, 로그인 시도
                accessServiceUseCase(token, type).collect {
                    emit(isNewUserResult)
                }
            } else {
                emit(isNewUserResult)
            }
        }
    }
}