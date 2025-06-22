package com.choius323.saisai.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.choius323.saisai.ui.component.SaiTopAppBar
import com.choius323.saisai.ui.navigation.BottomNavigationBar
import com.choius323.saisai.ui.navigation.MainNavController
import com.choius323.saisai.ui.navigation.rememberSaiNavController
import com.choius323.saisai.ui.theme.SaiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaiTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val mainNavController = rememberSaiNavController<MainNavController>()
    Scaffold(
        topBar = {
            SaiTopAppBar(
                mainNavController = mainNavController,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavigationBar(
                mainNavController = mainNavController, modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        MainNavController(
            navController = mainNavController,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(innerPadding),
        )
    }
}