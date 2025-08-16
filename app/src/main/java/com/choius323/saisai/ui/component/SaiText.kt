package com.choius323.saisai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.pretendardFamily

@Composable
fun SaiText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = FontWeight.Medium,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    fontFamily: FontFamily? = null,
    minLines: Int = 1,
) {
    val adjustedStyle = remember {
        style.copy(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
            lineHeight = 1.em,
        )
    }

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        maxLines = maxLines,
        style = adjustedStyle,
        letterSpacing = letterSpacing,
        fontFamily = fontFamily ?: adjustedStyle.fontFamily ?: pretendardFamily,
        minLines = 1,
    )
}

@Composable
fun SaiText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = FontWeight.Medium,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
) {
    val adjustStyle = remember {
        TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
            lineHeight = 1.em,
        )
    }

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        maxLines = maxLines,
        style = style.merge(adjustStyle)
    )
}

@Composable
fun TextRowBar(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SaiText(text = text, fontSize = 16.sp, color = Color.Gray)
        Icon(Icons.AutoMirrored.Rounded.ArrowForwardIos, "Arrow Forward")
    }
}

@Preview(showBackground = true)
@Composable
private fun TextRowBarPreview() {
    SaiTheme {
        Surface(
            Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier
                    .width(200.dp)
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SaiText(
                    "차단게시물 / 유저관리",
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.background(Color.Gray)
                )
                SaiText(
                    buildAnnotatedString { append("텍스트 입니다.") },
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.background(Color.Gray)
                )
            }
        }
    }
}