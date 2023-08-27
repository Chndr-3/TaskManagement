package com.example.taskmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.taskmanagement.ui.screen.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            HomeScreen()
        }
    }
}
