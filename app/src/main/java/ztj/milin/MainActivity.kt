package ztj.milin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ztj.milin.ui.theme.MilinTheme

class MainActivity : ComponentActivity() {
    var user = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()

        setContent {
            MilinTheme {
                // 创建 NavController
                val navController = rememberNavController()

                // 创建登录状态变量
                var isLoggedIn by rememberSaveable { mutableStateOf(false) }

                // 根据登录状态动态选择要显示的 Composable
                NavHost(navController = navController, startDestination = "login") {
                    // 登录界面
                    composable("login") {
                        Login(user) { username ->
                            user = username
                            isLoggedIn = true
                            navController.navigate("main")
                        }
                    }

                    // 主界面
                    composable("main") {
                        if (isLoggedIn) {
                            Main(user)
                        } else {
                            // 如果未登录，重定向到登录界面
                            navController.navigate("login")
                        }
                    }
                }
            }
        }
    }
}

