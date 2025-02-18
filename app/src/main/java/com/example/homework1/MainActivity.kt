package com.example.homework1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.homework1.Data.User
import com.example.homework1.Data.UserDatabase
import com.example.homework1.ui.theme.Homework1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var sensorHandler: SensorHandler
    private lateinit var notificationHandler: NotificationHandler

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        notificationHandler = NotificationHandler(this)
        sensorHandler = SensorHandler(this) {temperature ->
            handleTemperatureChange(temperature)
        }

        val db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "userData"
        ).build()

        val userDao = db.userDao()

        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) {
                userDao.getAll()
            }

            if(users.isEmpty()){
                withContext(Dispatchers.IO) {
                    userDao.addUser(User(1, "Janne", ""))
                }
            }
        }

        setContent {
            val navController = rememberNavController()
            Homework1Theme {
                NavHost(navController = navController, startDestination =  Routes.messages) {
                    composable(Routes.messages) {
                        Conversation(navController, SampleData.conversationSample)
                    }
                    composable(Routes.profile) {
                        ProfileScreen(navController, this@MainActivity)
                    }
                }
            }
        }
    }
    private fun handleTemperatureChange(temperature: Float) {
        if(temperature > 25) {
            Log.d("TAG", "handleTempChange")
            notificationHandler.sendNotification(
                "High temperature detected",
                "Temperature is high ($temperature degrees). Tap to open"
            )
        }
        else if(temperature < 10) {
            Log.d("TAG", "handleTempChange")
            notificationHandler.sendNotification(
                "Low temperature detected",
                "Temperature is low ($temperature degrees). Tap to open"
            )
        }
    }
}

