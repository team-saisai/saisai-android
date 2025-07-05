package com.choius323.saisai.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.choius323.saisai.util.PermissionDescriptionProvider

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionDescriptionProvider: PermissionDescriptionProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        confirmButton = {
            Button(
                onClick = {
                    if (isPermanentlyDeclined) {
                        onGoToAppSettingsClick()
                    } else {
                        onOkClick()
                    }
                },
                content = { SaiText("수락") },
            )
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                content = { SaiText("거부") },
            )
        },
        title = {
            SaiText(
                text = permissionDescriptionProvider.getTitle(context),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        text = {
            SaiText(
                text = permissionDescriptionProvider.getDescription(
                    context, isPermanentlyDeclined = isPermanentlyDeclined
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}