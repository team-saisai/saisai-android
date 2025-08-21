package com.choius323.saisai.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun EmptyCourseList(
    content: String,
    modifier: Modifier = Modifier,
    isShowImage: Boolean = true,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isShowImage) {
            Image(
                painter = painterResource(id = R.drawable.img_bicycle),
                contentDescription = "Empty course list image",
                modifier = Modifier.size(width = 201.dp, height = 132.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SaiText(
            text = content,
            fontSize = 16.sp,
            color = SaiColor.Gray00,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onClick,
            modifier = Modifier
                .widthIn(min = 264.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Lime
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            SaiText(
                text = "코스 보러가기",
                fontSize = 15.sp,
                color = SaiColor.Gray90,
                fontWeight = FontWeight.W600,
            )
        }
    }
}

@Preview
@Composable
fun EmptyCourseListPreview() {
    SaiTheme {
        EmptyCourseList(
            content = "저장한 코스가 없습니다.\n" +
                    "코스를 탐색해 나의 취향을 발견해보세요.",
            onClick = {},
            modifier = Modifier.size(1000.dp)
        )
    }
}
