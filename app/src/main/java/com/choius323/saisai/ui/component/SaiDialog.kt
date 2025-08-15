package com.choius323.saisai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun SaiDialog(
    isShow: Boolean,
    content: String,
    confirmButtonText: String,
    modifier: Modifier = Modifier,
    confirmButtonColor: Color = SaiColor.Lime,
    subContent: String? = null,
    isModal: Boolean = true,
    onClickButton: (isConfirm: Boolean) -> Unit
) {
    if (isShow) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0x804A4D50))
                .then(
                    if (isModal) {
                        Modifier.clickable(
                            indication = null,
                            interactionSource = null
                        ) { }
                    } else {
                        Modifier
                    }
                ),
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
                    text = content,
                    color = SaiColor.White,
                    fontSize = 18.sp,
                )
                if (subContent != null) {
                    Spacer(modifier = Modifier.height(7.dp))
                    SaiText(
                        text = subContent,
                        color = SaiColor.Gray40,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                DialogButtons(confirmButtonText, confirmButtonColor, Modifier, onClickButton)
            }
        }
    }
}

@Composable
private fun DialogButtons(
    confirmButtonText: String,
    confirmButtonColor: Color,
    modifier: Modifier = Modifier,
    onClickButton: (isConfirm: Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = { onClickButton(false) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Gray80
            ),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(
                text = "취소",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = SaiColor.Gray20
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onClickButton(true) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = confirmButtonColor
            ),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(
                text = confirmButtonText,
                color = SaiColor.Gray90,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun SaiDialogPreview() {
    SaiDialog(
        isShow = true,
        content = "정말로 로그아웃 하시겠어요?",
        confirmButtonText = "로그아웃",
        isModal = false,
        onClickButton = { }
    )
}

@Preview(heightDp = 400)
@Composable
fun SaiDialogPreviewWithSubContent() {
    SaiDialog(
        isShow = true,
        content = "정말로 로그아웃 하시겠어요?",
        confirmButtonText = "로그아웃",
        confirmButtonColor = SaiColor.Lime,
        subContent = "로그아웃 하시면 다시 로그인해야 해요.",
        isModal = true,
        onClickButton = { }
    )
}
