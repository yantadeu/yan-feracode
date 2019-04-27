@file:Suppress("DEPRECATION")

package com.yan.feracode.spotify.view.utils

import android.app.ActivityManager
import android.content.Context

object ServiceUtils {

    fun isAudioPlayerServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        var serviceState = false
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                serviceState = true
            }
        }
        return serviceState
    }
}
