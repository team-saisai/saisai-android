package com.choius323.saisai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import com.choius323.saisai.ui.navigation.MainNavController
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun SaiTopAppBar(
    mainNavController: MainNavController,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by mainNavController.navController.currentBackStackEntryAsState()
    navBackStackEntry?.let { entry ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { TopAppBarViewModel() },
        )
        SaiTopAppBar(
            viewModel.navigationIcon,
            viewModel.title,
            viewModel.actions,
            modifier
        )
    }
}

@Composable
private fun SaiTopAppBar(
    navigationIcon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(TopAppBarHeight)
            .padding(start = 24.dp, end = 14.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.align(Alignment.Center)) {
            title()
        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            navigationIcon()
            Spacer(Modifier.weight(1f))
            Row {
                actions()
            }
        }
    }
}

@Preview(widthDp = 300, showBackground = true)
@Preview(widthDp = 150, showBackground = true)
@Composable
private fun SaiTopAppBarPreview() {
    SaiTheme {
        Surface {
            SaiTopAppBar(
                navigationIcon = {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                },
                title = {
                    SaiText("Title Preview", maxLines = 1)
                },
                actions = {
                    Icon(Icons.AutoMirrored.Default.List, null)
                    Icon(Icons.Default.AddAlert, null)
                    Icon(Icons.AutoMirrored.Default.List, null)
                    Icon(Icons.Default.AddAlert, null)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(widthDp = 300, showBackground = true)
@Composable
private fun SaiTopAppBarPreview2() {
    SaiTheme {
        Surface {
            SaiTopAppBar(
                navigationIcon = {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                },
                title = {
                    SaiText("Title Preview 2", maxLines = 1)
                },
                actions = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private class TopAppBarViewModel : ViewModel() {
    var title by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())
    var navigationIcon by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())
    var actions by mutableStateOf<@Composable RowScope.() -> Unit>({ }, referentialEqualityPolicy())
}

@Composable
fun ProvideAppBar(
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopAppBarViewModel() },
        )
        LaunchedEffect(title, navigationIcon, actions) {
            viewModel.title = title
            viewModel.navigationIcon = navigationIcon
            viewModel.actions = actions
        }
    }
}

val TopAppBarHeight = 60.dp
