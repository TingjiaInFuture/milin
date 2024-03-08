package ztj.milin

import Api
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Explore(user: User) {
    // Todo:获取推荐的用户、热门的话题等

    LazyRow(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        item { BottleModule() } // 漂流瓶模块
        item { BridgeModule() } // 牵线搭桥模块
        // Todo:添加其他模块
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleModule() {
    val api = Api()

    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }
    var newMessage by remember { mutableStateOf("") }
    var showTextField by remember { mutableStateOf(false) } // 新增状态

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "漂流瓶",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            Text(
                text = message,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            if (showTextField) { // 根据状态决定是否显示TextField
                TextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    label = { Text("想说什么呢？") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        showTextField = true // 点击按钮后显示TextField
                        scope.launch {
                            val success = api.addBottle(newMessage)
                            if (success) {
                                newMessage = ""
                            }
                        }
                    }
                ) {
                    Text("扔一个")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            val bottle = api.getBottle()
                            message = bottle
                        }
                    }
                ) {
                    Text("捞一个")
                }
            }
        }
    }
}


@Composable
fun BridgeModule() {
    // Todo:实现牵线搭桥模块
}


