package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun Greeting(
    name: String?,
    location: String,
    temperature: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        SaiText(
            "안녕하세요 ${name?.plus("님") ?: ""}!\n오늘도 함께 달려보아요 :)",
            fontWeight = FontWeight.Medium,
            fontSize = 26.sp,
            lineHeight = 33.sp,
        )
        Row {
            Icon(
                Icons.Outlined.LocationOn,
                "현재 위치",
            )
            SaiText(" $location")
            Spacer(Modifier.width(18.dp))
            Icon(
                Icons.Outlined.Cloud,
                "현재 온도"
            )
            SaiText(" $temperature")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    SaiTheme {
        Surface {
            Greeting(
                name = "방문자",
                location = "서울시 강남구",
                temperature = "20°C",
            )
        }
    }
}