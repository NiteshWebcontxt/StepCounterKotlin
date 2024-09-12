package com.daniyalak.stepcounterkotlin_androidfitnessapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    const val PHYCAL_ACTIVITY_CODE =11
    const val NOTIFICATION_PERMISSION_CODE =12

    fun checkPhysicalPermission(context: Context): Boolean {
        return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PHYCAL_ACTIVITY_CODE
            )
            false
        } else {
            // Permission already granted
            true
        }
    }
    fun checkNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Request notification permission
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
                false
            } else {
                // Permission already granted
                true
            }
        } else {
            // No need to request for versions below Android 13
            true
        }
    }




}