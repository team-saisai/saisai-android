package com.choius323.saisai.ui.component

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LifecycleStartEffect

@Composable
fun ChangeStatusBarIconsColor() {
    val view = LocalView.current
    LifecycleStartEffect(Unit) {
        val insetsController =
            WindowCompat.getInsetsController((view.context as Activity).window, view)
        insetsController.isAppearanceLightStatusBars = true

        onStopOrDispose {
            insetsController.isAppearanceLightStatusBars = false
        }
    }
}