package com.github.brainku.kotlinforandroid.utils

import android.util.TypedValue
import com.github.brainku.kotlinforandroid.AppContext

/**
 * Created by zhengxinwei@N3072 on 2017/10/12.
 */

val Int.dp: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), AppContext.getInstance().resources.displayMetrics).toInt()
