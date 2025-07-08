package com.choius323.saisai.ui.screen.record

import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.GpxPoint

data class RecordUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseDetail: CourseDetail? = null,
    val nowPoint: GpxPoint = GpxPoint(0.0, 0.0, 0.0, 0.0),
    val permissionGranted: Boolean = false,
    val isRecording: Boolean = false,
    val startTime: Long = 0,
)

sealed interface RecordUiEvent {
    data object ClickedStart : RecordUiEvent
    data class PermissionChanged(val isGranted: Boolean) : RecordUiEvent
}

sealed interface RecordSideEffect {
    data object StartRecording : RecordSideEffect
}