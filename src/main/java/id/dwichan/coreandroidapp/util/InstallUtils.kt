package id.dwichan.coreandroidapp.util

import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build

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

    fun isInstallInEmulator(): Boolean {
        // Android SDK emulator
        return ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                && Build.FINGERPRINT.endsWith(":user/release-keys")
                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_")
                && Build.BRAND == "google"
                && Build.MODEL.startsWith("sdk_gphone_"))
                //
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                //bluestacks
                || "QC_Reference_Phone" == Build.BOARD
                && !"Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true) //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HOST.startsWith("Build") //MSI App Player
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk"
                // another Android SDK emulator check
                || SystemProperties.getProp("ro.kernel.qemu") == "1")
    }
}