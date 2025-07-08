package com.choius323.saisai.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.choius323.saisai.util.PermissionDescriptionProvider
import com.choius323.saisai.util.getPermissionMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionActions(
    permissionState: MultiplePermissionsState,
    isShowPermissionDialog: Boolean,
    setShowPermissionDialog: (Boolean) -> Unit,
    onPermissionAllGranted: () -> Unit,
) {
    val permissionMap = getPermissionMap(permissionState.permissions.map { it.permission })
    val context = LocalContext.current
    if (isShowPermissionDialog) {
        when {
            permissionState.allPermissionsGranted -> {
                onPermissionAllGranted()
                setShowPermissionDialog(false)
            }

            else -> {
                RevokedPermissionsDialog(
                    permissionState,
                    permissionMap,
                    context,
                    setShowPermissionDialog
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RevokedPermissionsDialog(
    permissionState: MultiplePermissionsState,
    permissionDescriptionProviderProviderMap: Map<String, PermissionDescriptionProvider>,
    context: Context,
    setShowPermissionDialog: (Boolean) -> Unit,
) {
    permissionState.revokedPermissions.firstOrNull { it.status.isGranted.not() }?.also { perm ->
        val descriptionProvider =
            permissionDescriptionProviderProviderMap[perm.permission] ?: return@also
        if (perm.status.isGranted) return@also

        PermissionDialog(
            permissionDescriptionProvider = descriptionProvider,
            isPermanentlyDeclined = perm.status.isGranted.not() && perm.status.shouldShowRationale,
            onDismiss = { setShowPermissionDialog(false) },
            onOkClick = {
                permissionState.launchMultiplePermissionRequest()
            },
            onGoToAppSettingsClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        )
    }
}