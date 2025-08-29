package com.choius323.saisai.ui.screen.badge_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.component.TopAppBarHeight
import com.choius323.saisai.ui.model.UserBadge
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
    BadgeListScreenContent(
        uiState, modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(top = TopAppBarHeight), viewModel::onEvent
    )
    BadgeDetailDialog(uiState.showBadgeDialog, Modifier.fillMaxSize()) {
        viewModel.onEvent(BadgeListUiEvent.CloseBadgeDialog)
    }
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
                .align(Alignment.TopCenter)
                .padding(top = 20.dp, start = 30.dp, end = 30.dp),
            contentPadding = PaddingValues(bottom = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            itemsIndexed(uiState.badgeList, { _, badge -> badge.id }) { index, badge ->
                BadgeGridItem(
                    badge, Modifier.clickable { onEvent(BadgeListUiEvent.OnClickBadge(badge)) }
                )
            }
        }
    }
}

@Composable
private fun BadgeGridItem(
    badge: UserBadge,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            badge.imageUrl ?: R.drawable.img_badge_lock,
            contentDescription = "뱃지 이미지",
            modifier = Modifier
                .widthIn(max = 94.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(12.dp))
        SaiText(
            badge.name,
            modifier = Modifier.widthIn(max = 94.dp),
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
        UserBadge.sample1.copy(imageUrl = R.drawable.ic_google),
        UserBadge.sample2,
        UserBadge.sample3,
        UserBadge.sample4,
    )
    SaiTheme {
        Surface {
            BadgeListScreenContent(uiState = BadgeListUiState(badgeList = badgeList)) {}
        }
    }
}