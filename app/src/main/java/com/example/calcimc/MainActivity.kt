package com.example.calcimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.calcimc.navigation.AppNavGraph
import com.example.calcimc.ui.theme.CalcIMCTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CalcIMCTheme {
                AppNavGraph()
            }
        }
    }
}
