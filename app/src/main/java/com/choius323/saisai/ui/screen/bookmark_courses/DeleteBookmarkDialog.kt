package com.choius323.saisai.ui.screen.bookmark_courses

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun DeleteBookmarkDialog(
    showDialog: Boolean,
    count: Int,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDialog) {
        Box(
            modifier
                .background(Color(0x804A4D50))
                .clickable(null, null) {},
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(320.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SaiColor.Gray85)
                    .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SaiText("${count}개의 코스를 삭제하시겠습니까?", lineHeight = 20.sp, fontSize = 18.sp)
                Spacer(Modifier.height(7.dp))
                SaiText(
                    "삭제 후에는 복구할 수 없습니다.",
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = SaiColor.Gray40
                )
                Spacer(Modifier.height(32.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Button(
                        onDismissRequest,
                        contentPadding = PaddingValues(vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaiColor.Gray80,
                            contentColor = SaiColor.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        SaiText("취소")
                    }
                    Button(
                        onConfirm,
                        contentPadding = PaddingValues(vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaiColor.Lime),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        SaiText("삭제")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DeleteBookmarkDialogPreview() {
    SaiTheme {
        Surface(
            modifier = Modifier.size(500.dp)
        ) {
            DeleteBookmarkDialog(
                showDialog = true,
                count = 5,
                onDismissRequest = {},
                onConfirm = {}
            )
        }
    }
}

