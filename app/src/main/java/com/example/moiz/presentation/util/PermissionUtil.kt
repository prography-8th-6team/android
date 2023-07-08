package com.example.moiz.presentation.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {

    const val REQUEST_CODE = 4412
    private val downloadPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf()
    } else {
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun hasCameraPermission(context: Context): Boolean {
        return context.checkPermissions(arrayOf(Manifest.permission.CAMERA))
    }

    fun requestCameraPermission(context: Activity) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE
        )
    }

}

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (!checkPermission(permission)) {
            return false
        }
    }
    return true
}