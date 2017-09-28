package com.github.brainku.kotlinforandroid.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlin.reflect.KClass

/**
 * Created by brainku on 17/9/28.
 */

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.launchActivity(clazz: KClass<out Activity>) {
    val intent = Intent(this, clazz.java)
    startActivity(intent)
}