package com.choius323.saisai.ui.screen.coursedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CourseDetailScreen(
    courseId: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        Text("Course Detail Screen\nCourse ID: $courseId")
        LazyColumn {
            items(List(50) { it }) {
                Text("Item $it")
            }
        }
    }
}