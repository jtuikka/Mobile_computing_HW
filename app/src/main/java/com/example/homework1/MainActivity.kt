package com.example.homework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homework1.ui.theme.Homework1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Homework1Theme {
                NavHost(navController = navController, startDestination =  Routes.messages) {
                    composable(Routes.messages) {
                        Conversation(navController, SampleData.conversationSample)
                    }
                    composable(Routes.profile) {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}

