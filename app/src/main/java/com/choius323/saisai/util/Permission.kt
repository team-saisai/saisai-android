package com.choius323.saisai.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import com.choius323.saisai.R

@Suppress("UNCHECKED_CAST")
fun getPermissionMap(permissions: List<String>) =
    permissions.associate {
        it to permissionsMap[it]
    }.filterValues {
        it != null
    } as Map<String, PermissionDescriptionProvider>

@SuppressLint("InlinedApi")
val permissionsMap = mapOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION to AccessFineLocationPermissionDescriptionProvider,
    android.Manifest.permission.ACCESS_COARSE_LOCATION to AccessCoarseLocationPermissionDescriptionProvider,
    android.Manifest.permission.POST_NOTIFICATIONS to PostNotificationPermissionDescriptionProvider,
)

val locationPermissions =
    listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )

val postNotificationPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    listOf(android.Manifest.permission.POST_NOTIFICATIONS)
} else {
    emptyList()
}

sealed interface PermissionDescriptionProvider {
    fun getTitle(context: Context): String
    fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String

    fun getDescription(
        context: Context,
        isPermanentlyDeclined: Boolean,
        @StringRes baseDescriptionRes: Int,
        @StringRes permanentlyDescriptionRes: Int,
    ): String {
        val baseDescription =
            context.getString(baseDescriptionRes)
        return if (isPermanentlyDeclined) {
            "$baseDescription\n${context.getString(permanentlyDescriptionRes)}"
        } else {
            baseDescription
        }
    }

    fun getTitle(context: Context, @StringRes titleRes: Int): String {
        return context.getString(titleRes)
    }
}

private object AccessFineLocationPermissionDescriptionProvider : PermissionDescriptionProvider {
    override fun getTitle(context: Context) =
        getTitle(context, R.string.permission_dialog_title_access_fine_location)

    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String =
        getDescription(
            context,
            isPermanentlyDeclined,
            R.string.permission_dialog_description_access_fine_location,
            R.string.permission_dialog_description_access_fine_location_permanently_declined
        )
}

private object AccessCoarseLocationPermissionDescriptionProvider : PermissionDescriptionProvider {
    override fun getTitle(context: Context) =
        getTitle(context, R.string.permission_dialog_title_access_coarse_location)

    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean) =
        getDescription(
            context,
            isPermanentlyDeclined,
            R.string.permission_dialog_description_access_coarse_location,
            R.string.permission_dialog_description_access_coarse_location_permanently_declined
        )
}

private object PostNotificationPermissionDescriptionProvider : PermissionDescriptionProvider {
    override fun getTitle(context: Context) =
        getTitle(context, R.string.permission_dialog_title_post_notification)


    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean) =
        getDescription(
            context,
            isPermanentlyDeclined,
            R.string.permission_dialog_description_post_notification,
            R.string.permission_dialog_description_post_notification_permanently_declined
        )
}