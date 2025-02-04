package com.example.homework1

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage
import com.example.homework1.Data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(navController: NavController, context: Context) {
    Profile(navController, context)
}

@Composable
fun Profile(navController: NavController, context: Context) {
    val userRepository = UserRepository(context)
    var username by remember { mutableStateOf("Default username") }
    var profilePictureURL by remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        username = withContext(Dispatchers.IO) {
            userRepository.getUsername(1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.messages) {
                        popUpTo(Routes.messages) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon"
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            AsyncImage(
                model = profilePictureURL,
                contentDescription = "Profile picture",
                modifier = Modifier.run {
                    size(200.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                                .border(
                                    width = 1.5.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            TextField(
                value = username,
                onValueChange = {username = it},
                label = { Text("Username")},
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        userRepository.updateUsername(username, 1)
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text("Save")
            }
        }
    }
}
