package com.choius323.saisai.util

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.choius323.saisai.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

object GoogleAccountUtil2 {
    // 발급받은 웹 클라이언트 ID
    private const val WEB_CLIENT_ID = BuildConfig.GOOGLE_WEB_CLIENT_ID

    suspend fun googleSignIn(
        context: Context,
        onSignedIn: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        val credentialManager = CredentialManager.create(context)

        // 로그인 기능에 대한 세부 설정을 담는 객체를 생성
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // 과거에 이 앱에 로그인을 허용한 적이 있는 계정만 사용 여부
            .setServerClientId(WEB_CLIENT_ID) // 웹 클라이언트 ID
            .setAutoSelectEnabled(true) // 계정 자동 선택 여부
            .setNonce(generateNonce())
            .build()

        // CredentialManager에게 전달할 최종 자격 증명 요청서를 생성
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request, context = context,
            )
            handleSignIn(result, onSignedIn, onError)
        } catch (e: GetCredentialException) {
            onError(e)
        }
    }

    private fun handleSignIn(
        result: GetCredentialResponse,
        onSignedIn: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        val credential = result.credential
        val responseJson: String

        when (credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                responseJson = credential.authenticationResponseJson
                Log.d(TAG, "Credential response: $responseJson")
            }

            // Password credential
            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                Log.d(TAG, "Username: $username, Password: $password")
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        Log.d(
                            TAG,
                            "Google ID token: ${googleIdTokenCredential.idToken} data: ${googleIdTokenCredential.data}"
                        )
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
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
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private const val TAG = "GoogleAccountUtil2"
}