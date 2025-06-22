package com.choius323.saisai.ui.screen.course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CourseScreen(modifier: Modifier = Modifier, onClickCourse: (Int) -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("Course Screen", Modifier.clickable { onClickCourse(-1) })
    }
}