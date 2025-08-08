package com.example.kotlinapp3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinapp3.ui.theme.KotlinApp3Theme
import com.example.kotlinapp3.ui.TaskList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinApp3Theme {
                TaskList()
            }
        }
    }
}
