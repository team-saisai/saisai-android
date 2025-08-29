package com.choius323.saisai.ui.screen.bookmark_courses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun DeleteSnackBar(
    count: Int,
    onClickDeleteAll: () -> Unit,
    onClickDeleteSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(SaiColor.Gray80)
            .padding(start = 20.dp, end = 14.dp, top = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (count == 0) {
                Spacer(Modifier.weight(1f))
                TextButton(
                    "전체 삭제",
                    onClick = onClickDeleteAll
                )
            } else {
                SaiText("${count}개 선택", fontSize = 16.sp)
                TextButton(
                    "선택 삭제",
                    color = Color(0xFFF47C7C),
                    onClick = onClickDeleteSelected
                )
            }
        }
    }
}

@Composable
private fun TextButton(text: String, color: Color = Color.Unspecified, onClick: () -> Unit) {
    SaiText(
        text, fontSize = 14.sp, color = color,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 11.5.dp, horizontal = 10.dp)
    )
}

@Composable
@Preview
private fun DeleteSnackbarPreview() {
    DeleteSnackBar(
        count = 3,
        onClickDeleteAll = {},
        onClickDeleteSelected = {}
    )
}

@Composable
@Preview
private fun DeleteSnackbarPreviewWithZeroCount() {
    DeleteSnackBar(count = 0, onClickDeleteAll = {}, onClickDeleteSelected = {})
}