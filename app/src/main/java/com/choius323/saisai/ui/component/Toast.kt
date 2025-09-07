package com.choius323.saisai.ui.component

import android.content.Context
import android.widget.Toast

fun Context.saiToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}