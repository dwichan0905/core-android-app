package id.dwichan.coreandroidapp.util

import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException


object InstallUtils {
    fun isFirstInstall(context: Context): Boolean {
        return try {
            val firstInstallTime: Long = context.packageManager
                .getPackageInfo(context.packageName, 0).firstInstallTime
            val lastUpdateTime: Long = context.packageManager
                .getPackageInfo(context.packageName, 0).lastUpdateTime
            firstInstallTime == lastUpdateTime
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            true
        }
    }

    fun isInstallFromUpdate(context: Context): Boolean {
        return try {
            val firstInstallTime: Long = context.packageManager
                .getPackageInfo(context.packageName, 0).firstInstallTime
            val lastUpdateTime: Long = context.packageManager
                .getPackageInfo(context.packageName, 0).lastUpdateTime
            firstInstallTime != lastUpdateTime
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}