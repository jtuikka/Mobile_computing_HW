package com.example.homework1

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.io.File

@Composable
fun ProfileScreen(navController: NavController, context: Context) {
    Profile(navController, context)
}

@Composable
fun Profile(navController: NavController, context: Context) {
    val userRepository = UserRepository(context)
    var username by remember { mutableStateOf("Default username") }
    var profilePictureURL by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        username = withContext(Dispatchers.IO) {
            userRepository.getUsername(1)
        }
        profilePictureURL = withContext(Dispatchers.IO) {
            userRepository.getProfilePicture(1)
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputFile = File(context.filesDir, "profile_picture.jpg")
            inputStream?.copyTo(outputFile.outputStream())

            profilePictureURL = outputFile.absolutePath
        }
    }

    val requestNotificationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
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
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                .size(200.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clickable { launcher.launch("image/*") }
        ){
            if (profilePictureURL != null) {
                AsyncImage(
                    model = profilePictureURL,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add picture",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
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
                        profilePictureURL?.let { nonNullURL ->
                            userRepository.updatePicture(nonNullURL, 1)
                        }
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
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        Toast.makeText(context, "Notification permission not needed for this version", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Enable notifications")
            }
        }
    }
}
