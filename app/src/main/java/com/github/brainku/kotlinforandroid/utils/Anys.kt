package com.github.brainku.kotlinforandroid.utils

import android.util.Log

/**
 * Created by brainku on 17/9/28.
 */

fun Any.logD(tag: String = "", info: String) {
    var newTag: String = tag
    if (tag.isEmpty()) {
        newTag = this::class.java.simpleName
    }
    Log.d(newTag, info)
}