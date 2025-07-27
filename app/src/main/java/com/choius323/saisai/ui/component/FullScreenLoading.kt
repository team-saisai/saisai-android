package com.choius323.saisai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiOpacity

@Composable
fun FullScreenLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SaiColor.Black.copy(alpha = SaiOpacity.Medium)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}