package ztj.milin

import ChatApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun Chat(user: User) {
    var selectedUser: User? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
    )
    {
        UserList(cuser = user,
            suser = selectedUser, users = userList,
            onUserSelected = { user ->
                selectedUser = user
            },
        )
    }
}

@Composable
fun UserList(cuser:User,suser: User?, users: List<User>, onUserSelected: (User) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color(0xFFADD8E6))
    ) {
        for (user in users) {
            Text(
                text = user.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        onUserSelected(user)
                    }
            )
            if (user == suser) {
                Conversation(cuser=cuser,suser = user)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Conversation(cuser:User,suser: User) {
    val chatApi = ChatApi()

    var conversation by remember { mutableStateOf(listOf<String>()) }
    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(cuser,suser) {
        scope.launch {
            try {
                val messages = chatApi.getMessages(suser)
                if (messages.isNotEmpty()) {
                    conversation = messages.filter { (it.sender == cuser.name || it.sender == suser.name) && (it.receiver == cuser.name || it.receiver == suser.name) }
                        .map { "${it.sender}: ${it.message}" }
                }


            } catch (e: Exception) {
                snackbarHostState.showSnackbar("An error occurred: ${e.message}")
            }
        }
    }

    // Create a LazyColumn for the conversation
    LazyColumn(modifier = Modifier.background(Color.Transparent)) {
        itemsIndexed(conversation) { index, m ->
            Text(
                text = AnnotatedString(m), // Replace "Your Text Here" with the actual text
                modifier = Modifier.fillMaxWidth(),
                color = Color.Yellow, // Set the desired text color
                fontSize = 16.sp, // Set the font size
                // Add other styling and parameters as needed
            )
        }
    }

    // 输入框
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Bottom
    ) {
        TextField(
            value = message,
            onValueChange = {
                message = it
            },
            placeholder = { Text("Type your message") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                if (message.isNotBlank()) {
                    scope.launch {
                        try {
                            val result = chatApi.addMessage(cuser, suser.name, message)
                            if (result) {
                                conversation = conversation + "You: $message"
                                message = ""
                            } else {
                                throw Exception("Message not sent")
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("An error occurred: ${e.message}")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Send")
        }
    }

    // 显示 Snackbar
    SnackbarHost(hostState = snackbarHostState)
}


