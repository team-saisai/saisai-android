package com.choius323.saisai.ui.screen.my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.UserProfile
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun MyPageProfileSection(
    userProfile: UserProfile,
    modifier: Modifier = Modifier,
    onClickProfile: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.Companion
                .size(106.dp)
                .clip(CircleShape)
                .border(1.4.dp, SaiColor.Gray70, CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = "Profile Image",
                modifier = Modifier.size(44.dp),
                tint = SaiColor.Gray80
            )
        }
        Spacer(Modifier.height(22.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onClickProfile)
        ) {
            SaiText(
                text = userProfile.nickname,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.size(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = "Edit Profile",
                modifier = Modifier.size(24.dp),
                tint = SaiColor.White
            )
        }

        Spacer(Modifier.height(8.dp))
        SaiText(
            text = userProfile.email,
            fontSize = 14.sp,
            color = SaiColor.Gray40,
            fontWeight = FontWeight.W300
        )
    }
}