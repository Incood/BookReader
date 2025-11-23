package com.example.avitotech.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.avitotech.navigation.MainApp
import com.example.avitotech.presentation.ui.theme.AvitoTechTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvitoTechTheme {
                MainApp()
            }
        }
    }
}