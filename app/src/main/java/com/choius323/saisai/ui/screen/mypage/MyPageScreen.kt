package com.choius323.saisai.ui.screen.mypage

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.ui.component.SaiText
import kotlinx.coroutines.launch

@Composable
fun MyPageScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    Button({ coroutineScope.launch { SessionManager.onLogout() } }) { SaiText("로그아웃") }
}