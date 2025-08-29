package com.choius323.saisai.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

val bottomNavigationBarHeight: Dp
    @Composable get() = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

val statusBarHeight: Dp
    @Composable get() = WindowInsets.statusBars.asPaddingValues().calculateBottomPadding()
