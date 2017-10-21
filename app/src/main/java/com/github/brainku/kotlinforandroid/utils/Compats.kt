package com.github.brainku.kotlinforandroid.utils

import android.os.Build
import com.github.brainku.kotlinforandroid.BuildConfig

/**
 * Created by brainku on 17/10/1.
 */

inline fun supportAPI25(code: () -> Unit) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        code()
    }
}

inline fun debug(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}