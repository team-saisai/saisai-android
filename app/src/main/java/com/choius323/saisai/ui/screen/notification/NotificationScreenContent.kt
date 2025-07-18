package com.choius323.saisai.ui.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.Notification
import com.choius323.saisai.ui.theme.SaiTheme
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
) {

}

@Composable
fun NotificationScreenContent(
    notifications: List<Notification>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(start = 22.dp)
    ) {
        items(notifications) { noti ->
            NotificationItem(
                notification = noti,
                modifier = Modifier
            )
        }
    }
}

/**
 * 개별 알림 항목을 표시하는 Composable 함수입니다.
 *
 * @param notification 표시할 알림 데이터입니다.
 */
@Composable
fun NotificationItem(notification: Notification, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = notification.imageUrl,
            contentDescription = "알림 이미지",
            modifier = Modifier
                .fillMaxHeight() // Row의 높이에 맞춥니다.
                .padding(vertical = 20.dp)
                .aspectRatio(1f) // 1:1 비율을 유지합니다.
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(16.dp))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    SaiText(
                        text = notification.date.format(DateTimeFormatter.ofPattern("yyyy. MM. dd")),
                        fontSize = 12.sp,
                        color = Color(0xFF51565B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SaiText(
                        text = notification.content,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (notification.unread) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFFC9FF66), CircleShape)
                    )
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Spacer(Modifier.width(22.dp))
            }
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF34383C))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenContentPreview() {
    val sampleNotifications = listOf(
        Notification(LocalDate.of(2025, 7, 7), null, "새로운 챌린지가 시작되었어요!", false),
        Notification(LocalDate.of(2025, 7, 7), null, "코스 완주를 축하합니다!!!!! 뱃지를 확인해보세요.", false),
        Notification(LocalDate.of(2025, 7, 6), null, "주간 랭킹이 갱신되었습니다.", true),
        Notification(LocalDate.of(2025, 7, 5), null, "친구가 회원님을 팔로우하기 시작했습니다.", true),
        Notification(LocalDate.of(2025, 7, 4), null, "새로운 이벤트에 참여해보세요!", false),
        Notification(LocalDate.of(2025, 7, 3), null, "회원님의 기록이 정상적으로 저장되었습니다.", true),
    )

    SaiTheme {
        Surface {
            NotificationScreenContent(notifications = sampleNotifications)
        }
    }
}