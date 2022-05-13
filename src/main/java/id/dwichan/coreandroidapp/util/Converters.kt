package id.dwichan.coreandroidapp.util

import java.text.SimpleDateFormat
import java.util.*

fun String.convertStringToDateTime(format: String): Date? {
    return SimpleDateFormat(format, Locale.getDefault()).parse(this)
}

fun Date.convertDateToString(format: String): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}