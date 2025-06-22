package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.model.Position
import com.choius323.saisai.ui.screen.map.MapScreen

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier.clickable(onClick = onBack)
            )
        })
    RecordScreenContent(
        route = listOf(Position(136.5, 36.5), Position(136.35, 36.2)),
        modifier = modifier
    )
}

@Composable
fun RecordScreenContent(
    route: List<Position>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        MapScreen(route, modifier = Modifier.fillMaxSize())
        Box(
            Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.BottomCenter)
        ) {
            Text("$route")
        }
    }
}