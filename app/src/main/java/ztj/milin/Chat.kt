package ztj.milin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Chat() {
    var selectedUser: User? by remember { mutableStateOf(null) }

    Column {
        // 用户列表
        UserList(users = userList, onUserSelected = { user ->
            selectedUser = user
        })

        // 对话区域
        selectedUser?.let { user ->
            Conversation(user = user)
        }
    }
}

@Composable
fun UserList(users: List<User>, onUserSelected: (User) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(Color(0xFFADD8E6))

    ) {
        itemsIndexed(users) { index, user ->
            Text(
                text = user.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        onUserSelected(user)
                    }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Conversation(user: User) {
    // 模拟对话记录
    val conversation = remember { mutableStateOf(listOf<String>()) }
    var message by remember { mutableStateOf("") }

    // Create a LazyColumn for the conversation
    LazyColumn(
    ) {
        items(conversation.value.size) {
            Text(
                text = AnnotatedString("Your Text Here"), // Replace "Your Text Here" with the actual text
                modifier = Modifier.fillMaxWidth(),
                color = Color.Yellow, // Set the desired text color
                fontSize = 16.sp, // Set the font size
                // Add other styling and parameters as needed

            )
        }
    }

    // 输入框
    Column(
        modifier = Modifier.fillMaxWidth(),
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
                    conversation.value = conversation.value + "You: $message"
                    message = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Send")
        }
    }
}
