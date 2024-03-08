package ztj.milin

import Api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(user: User, onLoginSuccess: (id: Int, name: String) -> Unit) {


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val api = Api()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it;user.name = it },
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
                var isLoading by remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        isLoading = true
                        CoroutineScope(Dispatchers.Main).launch {
                            val success = api.loginUser(user, password)
                            if (success != 0) {
                                // 登录成功
                                user.id = success
                                onLoginSuccess(user.id, username)
                                snackbarHostState.showSnackbar("登录成功")
                            } else {
                                // 登录失败
                                snackbarHostState.showSnackbar("用户名或密码错误")
                            }
                            isLoading = false
                        }
                    }, modifier = Modifier
                        .fillMaxWidth(0.5f) // 使用剩余宽度的 50%
                        .padding(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color(0xFF02B803),
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("登录")
                    }
                }

                Spacer(modifier = Modifier.width(10.dp)) // 在两个按钮之间添加一些间距

                Button(
                    onClick = {
                        // 在这里执行注册逻辑
                        CoroutineScope(Dispatchers.Main).launch {
                            val success = api.registerUser(user, password)

                            if (success > 0) {
                                // 注册成功
                                user.id = success
                                onLoginSuccess(user.id, username)
                            } else {
                                // 注册失败
                                val s = when (success) {
                                    -1 -> "用户名不能为空"
                                    -2 -> "密码不能为空"
                                    else -> "用户名已存在"
                                }
                                snackbarHostState.showSnackbar(s)
                            }
                        }
                    }, modifier = Modifier
                        .fillMaxWidth(0.8f) // 使用剩余宽度的 80%
                        .padding(8.dp)
                ) {
                    Text("注册")
                }
            }


        }
    }
    SnackbarHost(hostState = snackbarHostState)
}


