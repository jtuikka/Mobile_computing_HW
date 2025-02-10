package com.example.homework1

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import com.example.homework1.Data.UserRepository
import com.example.homework1.ui.theme.Homework1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    val userRepository = UserRepository(LocalContext.current)
    var profilePicture by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    LaunchedEffect(msg) {
        val picture = withContext(Dispatchers.IO) {
            userRepository.getProfilePicture(1)
        }
        profilePicture = picture
        val name = withContext(Dispatchers.IO) {
            userRepository.getUsername(1)
        }
        userName = name
    }
    Row(modifier = Modifier.padding(all = 8.dp)){
        if(profilePicture.isEmpty()){
            Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
        else{
            AsyncImage(
                model = profilePicture,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
        }


        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {


            Text(
                text = userName,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard(){
    Homework1Theme {
        Surface {
            MessageCard(msg = Message("Janne", "Moi, mitä kuuluu?"))
        }
    }
}

@Composable
fun Conversation(navController: NavController, messages: List<Message>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement =  Arrangement.End
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.profile) {
                        popUpTo(Routes.profile) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile Icon"
                )
            }
        }

        LazyColumn {
            items(messages) { message ->
                MessageCard(message)
            }
        }
    }
}

@Preview
@Composable
fun PreviewConversation(navController: NavController = rememberNavController()) {
    Homework1Theme {
        Conversation(navController, SampleData.conversationSample)
    }
}
