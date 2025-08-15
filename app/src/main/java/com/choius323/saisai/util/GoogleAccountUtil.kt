package com.choius323.saisai.util

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.choius323.saisai.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.security.SecureRandom

object GoogleAccountUtil {
    // 발급받은 웹 클라이언트 ID
    private const val WEB_CLIENT_ID = BuildConfig.GOOGLE_WEB_CLIENT_ID

    suspend fun googleSignIn(
        context: Context,
        onSignedIn: (idToken: String) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        val credentialManager = CredentialManager.create(context)

        // 로그인 기능에 대한 세부 설정을 담는 객체를 생성
        val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = WEB_CLIENT_ID // 웹 클라이언트 ID 사용
        ).setNonce(generateNonce())
            .build()

        // CredentialManager에게 전달할 최종 자격 증명 요청서를 생성
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request, context = context,
            )
            handleSignInWithGoogleOption(result, onSignedIn, onError)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error getting credential", e)
            onError(e)
        }
    }

    private fun handleSignInWithGoogleOption(
        result: GetCredentialResponse,
        onSignedIn: (idToken: String) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                // 미리 선언된 타입이 아니면 예외 처리
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        // idToken: String, data: Bundle
                        Log.d(
                            TAG,
                            "GoogleIdToken: ${googleIdTokenCredential.idToken}, data: ${googleIdTokenCredential.data}"
                        )
                        onSignedIn(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                        onError(e)
                    }
                } else {
                    val message = "Unexpected type of credential"
                    Log.e(TAG, message)
                    onError(Exception(message))
                }
            }

            else -> {
                val message = "Unexpected type of credential"
                Log.e(TAG, message)
                onError(Exception(message))
            }
        }
    }

    suspend fun googleSignOut(
        context: Context, onSuccess: () -> Unit = {}, onError: (Exception) -> Unit = {},
    ) {
        try {
            val credentialManager = CredentialManager.create(context)
            // 사용자 로그인 상태를 캐시(저장)해 둔 자격 증명 제공자(Credential Provider)의 상태를 제거
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Log.d("GoogleLogout", "Credential state cleared successfully.")
            onSuccess()
        } catch (e: ClearCredentialException) {
            Log.e("GoogleLogout", "Error clearing credential state", e)
            onError(e)
        }
    }

    // Nonce(Number used once) 랜덤 생성
    private fun generateNonce(length: Int = 16): String {
        val nonceBytes = ByteArray(length)
        SecureRandom().nextBytes(nonceBytes)
        return Base64.encodeToString(nonceBytes, Base64.URL_SAFE)
    }

    private const val TAG = "GoogleAccountUtil"
}