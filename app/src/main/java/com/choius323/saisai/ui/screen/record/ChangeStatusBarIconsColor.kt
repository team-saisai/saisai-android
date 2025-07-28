package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ChangeStatusBarIconsColor() {
    val isDarkTheme = isSystemInDarkTheme()
    val view = LocalView.current

    DisposableEffect(isDarkTheme) {
        val window = (view.context as android.app.Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        // isAppearanceLightStatusBars 값을 isDarkTheme 값과 반대로 설정합니다.
        // 다크 테마일 때(true) -> 아이콘을 어둡게(true)
        // 라이트 테마일 때(false) -> 아이콘을 밝게(false) - 기본 동작과 동일
        // 사용자의 요구사항은 "다크 테마일 때 검정색" 이므로 isDarkTheme일 때 true로 설정
        insetsController.isAppearanceLightStatusBars = isDarkTheme

        onDispose {
            // 이 컴포저블이 사라질 때, 원래 테마에 맞게 되돌립니다.
            // 만약 앱 전체에 걸쳐 동일한 규칙을 원한다면 onDispose 블록을 비워둘 수 있습니다.
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
        }
    }
}