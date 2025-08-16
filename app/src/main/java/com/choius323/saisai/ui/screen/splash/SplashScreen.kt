package com.choius323.saisai.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_background_full),
            contentDescription = "스플래시 배경 이미지",
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF222B40),
                                Color(0x00222B40)
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-50).dp)
        ) {
            SaiText(
                text = "사이사이",
                style = Typography.AppTitle.copy(fontSize = 42.sp, fontWeight = FontWeight.W400),
                color = SaiColor.White,
            )
            SaiText(
                text = "일상 사이 스며드는 라이딩 코스",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                color = SaiColor.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SaiTheme {
        SplashScreen(onSplashFinished = {})
    }
}