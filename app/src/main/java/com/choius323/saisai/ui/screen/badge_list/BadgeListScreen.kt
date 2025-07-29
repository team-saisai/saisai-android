package com.choius323.saisai.ui.screen.badge_list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.model.UserBadgeDetail
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BadgeListScreen(
    modifier: Modifier = Modifier,
    viewModel: BadgeListViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is BadgeListSideEffect.GoBack -> goBack
            is BadgeListSideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }
        }
    }
    ProvideAppBar(navigationIcon = {
        Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            "뒤로 가기",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = goBack)
        )
    }, title = {
        SaiText("나의 뱃지", fontWeight = FontWeight.W500, fontSize = 18.sp)
    })
    BadgeListScreenContent(uiState, modifier, viewModel::onEvent)
    if (uiState.isLoading) {
        FullScreenLoading()
    }
}

@Composable
private fun BadgeListScreenContent(
    uiState: BadgeListUiState,
    modifier: Modifier = Modifier,
    onEvent: (BadgeListUiEvent) -> Unit,
) {
    Box(modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp),
        ) {
            itemsIndexed(uiState.badgeList, { index, badge -> badge.id }) { index, badge ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    // 아이템의 위치(index)에 따라 정렬을 다르게 적용합니다.
                    contentAlignment = when (index % 3) {
                        0 -> Alignment.CenterStart
                        1 -> Alignment.Center
                        else -> Alignment.CenterEnd
                    }
                ) {
                    BadgeGridItem(badge)
                }
            }
        }
        if (uiState.showBadgeDetail != null) {
            BadgeDetailDialog(uiState.showBadgeDetail, Modifier.fillMaxSize()) {
                onEvent(BadgeListUiEvent.CloseBadgeDialog)
            }
        }
    }
}

@Composable
private fun BadgeGridItem(
    badge: UserBadge,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            badge.imageUrl,
            contentDescription = "뱃지 이미지",
            modifier = Modifier
                .widthIn(max = 94.dp)
                .aspectRatio(1f)
                .border(1.dp, SaiColor.Gray50, CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(12.dp))
        SaiText(
            badge.name,
            color = SaiColor.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun BadgeGridItemPreview() {
    SaiTheme {
        Surface {
            BadgeGridItem(badge = UserBadge.sample1)
        }
    }
}

@Preview
@Composable
private fun BadgeListScreenContentPreview() {
    val badgeList = listOf(
        UserBadge.sample1, UserBadge.sample2, UserBadge.sample3,
        UserBadge.sample4, UserBadge.sample5, UserBadge.sample6,
        UserBadge.sample7,
    )
    SaiTheme {
        Surface {
            BadgeListScreenContent(uiState = BadgeListUiState(badgeList = badgeList)) {}
        }
    }
}

@Preview
@Composable
private fun BadgeListScreenContentDialogPreview() {
    val badgeList = listOf(
        UserBadge.sample1, UserBadge.sample2, UserBadge.sample3,
        UserBadge.sample4, UserBadge.sample5, UserBadge.sample6,
        UserBadge.sample7,
    )
    SaiTheme {
        Surface {
            BadgeListScreenContent(
                uiState = BadgeListUiState(
                    badgeList = badgeList,
                    showBadgeDetail = UserBadgeDetail.sample1
                )
            ) {}
        }
    }
}