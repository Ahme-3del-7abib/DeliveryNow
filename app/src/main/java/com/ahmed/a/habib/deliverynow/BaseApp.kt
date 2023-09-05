package com.ahmed.a.habib.deliverynow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : Application() {

    companion object {
        fun getUserType() = BuildConfig.userType.toString()
    }
}