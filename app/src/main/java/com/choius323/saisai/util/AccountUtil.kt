package com.choius323.saisai.util

import android.content.Context
import com.choius323.saisai.ui.model.LoginType

object AccountUtil {
    suspend fun deleteAccount(
        context: Context,
        loginType: LoginType,
        onSuccess: (accessToken: String) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) = when (loginType) {
        LoginType.GOOGLE -> {
            GoogleAccountUtil.googleSignIn(context, onSuccess, onFailure)
        }

        LoginType.KAKAO -> {
            KakaoAccountUtil.kakaoLogin(context, onSuccess, onFailure)
        }
    }
}