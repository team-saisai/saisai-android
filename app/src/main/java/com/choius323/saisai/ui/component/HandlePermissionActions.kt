package com.choius323.saisai.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    showPermissionDialog: MutableState<Boolean>,
    onPermissionAllGranted: () -> Unit,
) {
    val permissionMap = getPermissionMap(permissionState.permissions.map { it.permission })
    val context = LocalContext.current
    if (showPermissionDialog.value) {
        when {
            permissionState.allPermissionsGranted -> {
                if (showPermissionDialog.value) showPermissionDialog.value = false
                onPermissionAllGranted()
            }

            else -> {
                RevokedPermissionsDialog(
                    permissionState,
                    permissionMap,
                    context,
                    showPermissionDialog
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
    showPermissionDialog: MutableState<Boolean>,
) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}

    permissionState.revokedPermissions.firstOrNull { it.status.isGranted.not() }?.also { perm ->
        val descriptionProvider =
            permissionDescriptionProviderProviderMap[perm.permission] ?: return@also
        if (perm.status.isGranted) return@also

        PermissionDialog(
            permissionDescriptionProvider = descriptionProvider,
            isPermanentlyDeclined = perm.status.isGranted.not() && perm.status.shouldShowRationale,
            onDismiss = { showPermissionDialog.value = false },
            onOkClick = {
//                permissionState.launchMultiplePermissionRequest()
                val array = permissionState.permissions.map { it.permission }.toTypedArray()
                requestPermissionLauncher.launch(array)
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