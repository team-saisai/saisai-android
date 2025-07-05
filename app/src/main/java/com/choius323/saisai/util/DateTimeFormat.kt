package com.choius323.saisai.util

import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

object DateTimeFormat {
    val monthDay = DateTimeFormatter.ofPattern("M/d", Locale.KOREA)!!
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)!!
}