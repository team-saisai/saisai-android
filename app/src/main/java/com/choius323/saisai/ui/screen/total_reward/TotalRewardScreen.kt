package com.choius323.saisai.ui.screen.total_reward

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.RewardInfo
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TotalRewardScreen(
    modifier: Modifier = Modifier,
    viewModel: TotalRewardViewModel = koinViewModel(),
    goToCourseList: () -> Unit,
    goBack: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TotalRewardSideEffect.GoBack -> goBack()
            TotalRewardSideEffect.GoToCourseList -> goToCourseList()
            is TotalRewardSideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }
        }
    }

    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "뒤로 가기",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = { viewModel.onEvent(TotalRewardUiEvent.OnClickBack) })
                    .padding(8.dp),
            )
        },
        title = {
            SaiText(
                "리워드 포인트",
                fontSize = 18.sp,
                color = SaiColor.White
            )
        }
    )
    TotalRewardScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier.padding(start = 24.dp, end = 22.dp)
    )
    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
    }
}

@Composable
private fun TotalRewardScreenContent(
    uiState: TotalRewardUiState,
    modifier: Modifier = Modifier,
    onEvent: (TotalRewardUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TotalRewardHeader(totalPoints = uiState.totalPoints)
        Spacer(modifier = Modifier.height(32.dp))
        RewardListSection(
            rewardInfoList = uiState.rewardInfoList,
            modifier = Modifier.fillMaxSize()
        ) {
            onEvent(TotalRewardUiEvent.OnClickEmpty)
        }
    }
}

@Composable
private fun TotalRewardHeader(totalPoints: Int) {
    val totalPointsFormatted = "${NumberFormat.getNumberInstance(Locale.US).format(totalPoints)}P"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(R.drawable.ic_reward_p),
            "포인트 아이콘",
            Modifier.size(90.dp),
            tint = SaiColor.Gray20
        )
        Spacer(modifier = Modifier.height(12.dp))
        SaiText(text = "나의 리워드", fontSize = 12.sp, color = SaiColor.Gray60)
        Spacer(modifier = Modifier.height(2.dp))
        SaiText(
            text = totalPointsFormatted,
            fontSize = 22.sp,
            color = SaiColor.Lime
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326)
@Composable
private fun TotalRewardScreenPreview() {
    SaiTheme {
        TotalRewardScreenContent(
            uiState = TotalRewardUiState(
                totalPoints = 1200,
                rewardInfoList = listOf(
                    RewardInfo.sample1,
                    RewardInfo.sample2,
                    RewardInfo.sample3
                )
            ),
        ) {}
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326)
@Composable
private fun TotalRewardHeaderPreview() {
    AndroidThreeTen.init(LocalContext.current)
    SaiTheme {
        TotalRewardHeader(totalPoints = 1200)
    }
}

