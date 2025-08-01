package com.choius323.saisai.ui.screen.notification_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.Notification
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun NotificationListScreen(
    modifier: Modifier = Modifier,
    goBack: () -> Unit = {},
) {
    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Default.ArrowBackIos,
                "뒤로 가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = goBack)
            )
        }
    )
    NotificationListScreenContent(
        notifications = listOf(
            Notification.sample1, Notification.sample2, Notification.sample3,
            Notification.sample4, Notification.sample5, Notification.sample6
        ),
        modifier = modifier
    )
}

@Composable
fun NotificationListScreenContent(
    notifications: List<Notification>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(start = 22.dp)
    ) {
        items(notifications) { notification ->
            NotificationItem(
                notification = notification,
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
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(16.dp))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 21.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    SaiText(
                        text = notification.date.format(DateTimeFormatter.ofPattern("yyyy. MM. dd")),
                        fontSize = 12.sp,
                        color = SaiColor.Gray70
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SaiText(
                        text = notification.content,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (notification.unread) {
                    Spacer(Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(SaiColor.Lime, CircleShape)
                    )
                }
                Spacer(Modifier.width(22.dp))
            }
            HorizontalDivider(thickness = 1.dp, color = SaiColor.Gray80)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenContentPreview() {
    val sampleNotifications = listOf(
        Notification.sample1, Notification.sample2, Notification.sample3,
        Notification.sample4, Notification.sample5, Notification.sample6
    )

    SaiTheme {
        Surface {
            NotificationListScreenContent(notifications = sampleNotifications)
        }
    }
}