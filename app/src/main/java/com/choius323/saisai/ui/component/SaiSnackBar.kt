package com.choius323.saisai.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SaiSnackBar(
    messageTxt: String,
    @DrawableRes iconRes: Int? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .padding(12.dp)
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "toastIcon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 14.dp, end = 7.dp)
            )
        }
        SaiText(
            text = messageTxt,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black
            )
        )
    }
}