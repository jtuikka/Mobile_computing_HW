package com.example.homework1

import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {

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


        super.onCreate(savedInstanceState)
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
}

