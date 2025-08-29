package com.choius323.saisai.ui.component

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.util.PermissionDescriptionProvider

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionDescriptionProvider: PermissionDescriptionProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    val context = LocalContext.current
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
                text = permissionDescriptionProvider.getTitle(context),
                color = SaiColor.White,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(7.dp))
            SaiText(
                text = permissionDescriptionProvider.getDescription(context, isPermanentlyDeclined),
                color = SaiColor.Gray40,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
            )
            Spacer(modifier = Modifier.height(32.dp))
            PermissionDialogButtons(Modifier) { isConfirm ->
                if (isConfirm) {
                    if (isPermanentlyDeclined) {
                        onGoToAppSettingsClick()
                    } else {
                        onOkClick()
                    }
                } else {
                    onDismiss()
                }
            }
        }
    }
}

@Composable
private fun PermissionDialogButtons(
    modifier: Modifier = Modifier,
    onClickButton: (isConfirm: Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onClickButton(false) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Gray80,
                contentColor = SaiColor.Gray20,
            ),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(text = "거부", fontSize = 16.sp, fontWeight = FontWeight.W600)
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
                text = "수락",
                color = SaiColor.Gray90,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600
            )
        }
    }
}