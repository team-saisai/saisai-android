package com.choius323.saisai.ui.screen.course_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun CourseDetailCautionDialog(
    isShowDialog: Boolean,
    modifier: Modifier = Modifier,
    onClickButton: (Boolean, Boolean) -> Unit,
) {
    if (isShowDialog.not()) return
    var isCheckedNoMoreShow by remember { mutableStateOf(false) }
    Box(
        modifier
            .background(Color(0x4A4D5080))
            .clickable(interactionSource = null, indication = null) {
                onClickButton(false, false)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .clickable(interactionSource = null, indication = null) {}
                .clip(RoundedCornerShape(20.dp))
                .width(320.dp)
                .background(SaiColor.Gray90)
                .padding(top = 36.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
        ) {
            SaiText(
                "주행 안내사항",
                Modifier.align(Alignment.CenterHorizontally),
                fontSize = 18.sp,
                color = SaiColor.White
            )
            Spacer(Modifier.height(16.dp))
            CautionDialogItemList()
            Spacer(Modifier.height(34.dp))
            CheckboxRow(isCheckedNoMoreShow, Modifier.align(Alignment.CenterHorizontally)) {
                isCheckedNoMoreShow = isCheckedNoMoreShow.not()
            }
            Spacer(Modifier.height(6.dp))
            TextButton(
                { onClickButton(true, isCheckedNoMoreShow) },
                Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SaiColor.Lime,
                    contentColor = SaiColor.Gray90,
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                SaiText("코스 도전 시작", fontSize = 16.sp, fontWeight = FontWeight.W600)
            }
        }
    }
}

@Composable
private fun CheckboxRow(
    isCheckedNoMoreShow: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
            .padding(end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (isCheckedNoMoreShow) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
            "오늘 하루 동안 보지 않기",
            Modifier.size(16.dp),
            tint = if (isCheckedNoMoreShow) SaiColor.White else SaiColor.Gray40,
        )
        Spacer(Modifier.width(4.dp))
        SaiText("오늘 하루 동안 보지 않기", color = SaiColor.Gray10, fontSize = 13.sp)
    }
}


@Composable
private fun CautionDialogItemList(modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // TODO: 아이템 목록 내용 변경
        CautionDialogItem("코스를 이탈한 경우,\n 주행이 자동으로 일시정지돼요.")
        CautionDialogItem("일시 정지 후 12시간이 지나면\n주행 기록이 초기화돼요.")
        CautionDialogItem("일시 정지 된 코스는\n코스 기록에서 확인할 수 있어요.")
    }
}

@Composable
private fun CautionDialogItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.ic_ride_bicycle), null, Modifier.size(32.dp))
        Spacer(Modifier.width(12.dp))
        SaiText(
            text,
            fontSize = 14.sp,
            color = SaiColor.White,
            fontWeight = FontWeight.W400,
            lineHeight = 20.sp
        )
    }
}

@Preview
@Composable
fun CourseDetailCautionDialogPreview() {
    CourseDetailCautionDialog(
        true,
        onClickButton = { _, _ -> }
    )
}
