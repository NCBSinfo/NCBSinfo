package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

fun View.hideMe() {
    this.visibility = View.GONE
}

fun View.showMe() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Calendar.serverTimestamp(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
    return format.format(Date(this.timeInMillis))
}


fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun ViewGroup.inflate(layout: Int) = LayoutInflater.from(this.context).inflate(layout, this, false)!!