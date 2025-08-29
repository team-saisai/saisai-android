package com.choius323.saisai.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiOpacity
import kotlinx.coroutines.delay

@Composable
fun FullScreenLoading(modifier: Modifier = Modifier, isModal: Boolean = false) {
    // 0.3초가 지나면 상태를 true로 변경하여 다이얼로그를 표시
    var shouldShow by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(300L)
        shouldShow = true
    }

    val interactionModifier = if (isModal) {
        Modifier
            .background(SaiColor.Gray60.copy(alpha = SaiOpacity.Medium))
            .clickable(interactionSource = null, indication = null, onClick = {})
    } else {
        Modifier
    }
    Dialog(
        {},
        DialogProperties(decorFitsSystemWindows = false, usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .then(interactionModifier),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = shouldShow,
                enter = fadeIn()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}