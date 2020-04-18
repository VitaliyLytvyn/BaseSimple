package com.us.telemedicine.global.extention

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.nullIfBlank() = if(isBlank()) null else this

fun parseDateFromString(dateString: String?): Date? {
    dateString ?: return null
    val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    var date: Date? = null
    date = try {
        SimpleDateFormat(dateFormat).parse(dateString)
    } catch (ex: ParseException) {
        null
    }
    return date
}

fun String.asDate() = parseDateFromString(this)