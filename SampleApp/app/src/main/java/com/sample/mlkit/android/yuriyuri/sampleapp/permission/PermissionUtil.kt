package com.sample.mlkit.android.yuriyuri.sampleapp.permission

import android.os.Build
import androidx.core.content.PermissionChecker
import androidx.appcompat.app.AppCompatActivity

class PermissionUtil constructor(private val activity: AppCompatActivity) {

    /**
     * すでにパーミッションが得られているかチェック(単体)
     */
    fun isAuthorized(permission: Permission): Boolean {
        return when (PermissionChecker.checkSelfPermission(activity.applicationContext, permission.permission)) {
            PermissionChecker.PERMISSION_GRANTED -> true
            else -> false
        }
    }

    /**
     * すでにパーミッションが得られているかチェック(複数)
     */
    fun isAuthorized(permissions: Array<Permission>): Boolean {
        var granted = true
        permissions.forEach { granted = granted.and(isAuthorized(it)) }
        return granted
    }

    /**
     * パーミッションの取得
     */
    fun requestPermission(permission: Permission, requestCd: Int) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            activity.requestPermissions(arrayOf(permission.permission), requestCd)
        }
    }

    /** 権限要求を「これ以上表示しない」設定にされているか確認 */
    fun showRationale(permission: Permission): Boolean {
        if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) return true
        return activity.shouldShowRequestPermissionRationale(permission.permission)
    }

    /** 権限要求結果の確認 */
    fun verifyGrantResults(results: IntArray): Boolean {
        if (results.isEmpty()) return false
        results.forEach { if (PermissionChecker.PERMISSION_GRANTED != it) return false }
        return true
    }
}