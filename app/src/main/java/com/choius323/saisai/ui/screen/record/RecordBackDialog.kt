package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun RecordBackDialog(
    isShow: Boolean,
    modifier: Modifier = Modifier,
    onClickButton: (isConfirm: Boolean) -> Unit
) {
    if (isShow) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0x804A4D50))
                .clickable(
                    indication = null,
                    interactionSource = null,
                ) {},
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .widthIn(max = 360.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SaiColor.Gray85)
                    .padding(top = 30.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SaiText(
                    text = "코스를 중단하고 나가시겠습니까?",
                    color = SaiColor.White,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(7.dp))
                SaiText(
                    text = "코스는 홈에서 이어서 도전할 수 있습니다.",
                    color = SaiColor.Gray40,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                )
                Spacer(modifier = Modifier.height(32.dp))
                BackDialogButtons(Modifier, onClickButton)
            }
        }
    }
}

@Composable
private fun BackDialogButtons(
    modifier: Modifier = Modifier,
    onClickButton: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onClickButton(false) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Gray80
            ),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(text = "취소", fontSize = 16.sp, fontWeight = FontWeight.W600)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onClickButton(true) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Lime
            ),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "나가기",
                color = SaiColor.Gray90,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600
            )
        }
    }
}

@Preview(device = "spec:width=1080px,height=1080px,dpi=440")
@Composable
fun RecordBackDialogPreview() {
    RecordBackDialog(
        isShow = true,
        onClickButton = {}
    )
}
