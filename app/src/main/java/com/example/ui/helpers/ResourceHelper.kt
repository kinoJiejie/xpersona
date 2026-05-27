package com.example.ui.helpers

import com.example.R

fun getPortraitDrawableId(resName: String): Int {
    return when (resName) {
        "img_minimal_pro" -> R.drawable.img_minimal_pro
        "img_street_style" -> R.drawable.img_street_style
        "img_biz_headshot" -> R.drawable.img_biz_headshot
        "img_creator_port" -> R.drawable.img_creator_port
        "img_fashion_port" -> R.drawable.img_fashion_port
        else -> R.drawable.ic_launcher_background // Fallback
    }
}
