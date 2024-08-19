package com.holidai.jejuway.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toDateYyyyMmDd(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

fun String.toShortDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = sdf.parse(this)
    val newSdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    return newSdf.format(date!!)
}