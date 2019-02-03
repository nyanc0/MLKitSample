package com.sample.mlkit.android.nyanc0.mlkitsample.permission

import android.Manifest
import android.text.TextUtils

enum class Permission(val permission: String) {

    // CAMERA
    P_CAMERA(Manifest.permission.CAMERA);

    companion object {
        fun get(permission: String): Permission? {
            values().forEach {
                if (TextUtils.equals(it.permission, permission)) return it
            }
            return null
        }
    }
}