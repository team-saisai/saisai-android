package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun RecordStateDescription(
    uiState: RecordUiState,
    modifier: Modifier = Modifier,
) {
    val courseDetail = remember { uiState.courseDetail }
    val green = remember { Color(0xFFC9FF66) }
    if (courseDetail == null) return
    val distance by remember(uiState.totalRideDistance) {
        derivedStateOf {
            buildAnnotatedString {
                withStyle(SpanStyle(color = green)) {
                    append("${uiState.totalRideDistance}")
                }
                append(" / ${courseDetail.distance}km")
            }
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(18.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(distance, fontSize = 24.sp)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF51565B))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth((uiState.totalRideDistance / courseDetail.distance).toFloat())
                        .fillMaxHeight()
                        .background(green)
                )
            }
            Spacer(Modifier.height(20.dp))
            Row {
                Icon(Icons.Default.Route, null)
                SaiText("다음 체크포인트: ")
            }
        }
    }
}

@Preview
@Composable
private fun RecordStateDescriptionPreview() {
    SaiTheme {
        Surface {
            RecordStateDescription(
                uiState = RecordUiState(
                    courseDetail = CourseDetail.sample,
                    totalRideDistance = 5.3
                ),
            )
        }
    }
}
