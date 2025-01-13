package com.tijiebo.woofwoof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tijiebo.woofwoof.ui.ComposeApp
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WoofwoofScreen {
                ComposeApp()
            }
        }
    }
}
