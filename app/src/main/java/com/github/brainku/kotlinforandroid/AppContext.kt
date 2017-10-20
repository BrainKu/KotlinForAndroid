package com.github.brainku.kotlinforandroid

import android.app.Application

/**
 * Created by zhengxinwei@N3072 on 2017/10/12.
 */

class AppContext: Application() {

    companion object {
        lateinit var sInstance: AppContext

        fun getInstance(): AppContext {
            return sInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }
}