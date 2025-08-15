package com.choius323.saisai.util

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

object KakaoAccountUtil {
    fun kakaoLogin(
        context: Context,
        onSuccess: (accessToken: String) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                onError(error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                onSuccess(token.accessToken)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        return if (UserApiClient.Companion.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.Companion.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.Companion.instance.loginWithKakaoAccount(
                        context,
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}\n")
                    onSuccess(token.accessToken)
                }
            }
        } else {
            UserApiClient.Companion.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun logout(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) = UserApiClient.Companion.instance.logout { error ->
        if (error != null) {
            Log.e(TAG, "로그아웃 실패. SDK에서 토큰 폐기됨", error)
            onError(error)
        } else {
            Log.i(TAG, "로그아웃 성공. SDK에서 토큰 폐기됨")
            onSuccess()
        }
    }

    fun unlink(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) = UserApiClient.instance.unlink { error ->
        if (error != null) {
            Log.e(TAG, "연결 해제 실패", error)
            onError(error)
        } else {
            Log.i(TAG, "연결 해제 성공. SDK에서 토큰 폐기 됨")
            onSuccess()
        }
    }

    private const val TAG = "KakaoLoginUtil"
}