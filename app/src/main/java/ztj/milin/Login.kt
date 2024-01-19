package ztj.milin

import ChatApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(user: User, onLoginSuccess: (id:Int,name: String) -> Unit) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // 设置状态栏为透明，并根据主题颜色设置状态栏图标颜色
        systemUiController.setSystemBarsColor(
            color = Color(0xFFADD8E6)
        )
    }


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val chatApi = ChatApi()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6)) // 设置背景颜色
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(16.dp), // 可根据需要进行调整
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(16.dp),

                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = username,
                    onValueChange = { username = it ;user.name = it},
                    label = { Text("用户名") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = {
                            // 在这里执行登录逻辑
                            CoroutineScope(Dispatchers.Main).launch {
                                val success = chatApi.loginUser(user, password)
                                // 验证用户名和密码，然后根据结果采取操作
                                if (success!=0) {
                                    // 登录成功
                                    user.id = success
                                    onLoginSuccess(user.id,username)
                                } else {
                                    // 登录失败，可以显示错误消息
                                    snackbarHostState.showSnackbar("用户名或密码错误")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f) // 使用屏幕宽度的 40%
                            .padding(8.dp)
                    ) {
                        Text("登录")
                    }

                    Spacer(modifier = Modifier.width(10.dp)) // 在两个按钮之间添加一些间距

                    Button(
                        onClick = {
                            // 在这里执行注册逻辑
                            CoroutineScope(Dispatchers.Main).launch {
                                val success = chatApi.registerUser(user, password)

                                if (success!=0) {
                                    // 注册成功
                                    user.id = success
                                    onLoginSuccess(user.id,username)
                                } else {
                                    // 注册失败，可以显示错误消息
                                    snackbarHostState.showSnackbar("用户名已存在")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // 使用屏幕宽度的 40%
                            .padding(8.dp)
                    ) {
                        Text("注册")
                    }
                }

            }
        }
    }
    SnackbarHost(hostState = snackbarHostState)
}


