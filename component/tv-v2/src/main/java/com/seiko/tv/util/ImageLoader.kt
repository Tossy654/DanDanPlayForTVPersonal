package com.seiko.tv.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadImage(view: View, url: String?) {
    Glide.with(view).load(url).into(this)
}