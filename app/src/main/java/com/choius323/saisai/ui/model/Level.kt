package com.choius323.saisai.ui.model

import androidx.compose.ui.graphics.Color
import com.choius323.saisai.ui.theme.SaiColor

sealed class Level(val displayText: String, val color: Color) {
    object Low : Level("하", SaiColor.Gray30)
    object Medium : Level("중", Color(0xFFF4E47C))
    object High : Level("상", Color(0xFFF47C7C))

    // 정수(Int) 값을 Difficulty 객체로 변환해주는 함수
    companion object {
        fun from(level: Int): Level {
            return when (level) {
                1 -> Low
                2 -> Medium
                3 -> High
                else -> Low // 기본값은 '하'로 처리
            }
        }
    }
}