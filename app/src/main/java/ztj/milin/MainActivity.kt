package ztj.milin

import Api
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ztj.milin.ui.theme.MilinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        WindowCompat.setDecorFitsSystemWindows(window, false)//状态栏没必要隐藏


//        //预备工作
//        main()

        // 从SharedPreferences中获取保存的用户信息
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        setContent {
            var userId by remember { mutableIntStateOf(sharedPreferences.getInt("userId", 0)) }
            var username by remember {
                mutableStateOf(
                    sharedPreferences.getString("username", "NaN") ?: "NaN"
                )
            }//强制类型安全

            LaunchedEffect(Unit) {
                // 检查用户是否存在
                if (userId != 0) {
                    val api = Api()
                    val id = api.checkUser(username)
                    if (id == 0) {
                        // 用户不存在，清除用户信息
                        with(sharedPreferences.edit()) {
                            remove("userId")
                            remove("username")
                            apply()
                        }
                        userId = 0
                        username = "NaN"
                    }
                }
            }
            LaunchedEffect(key1 = true) {
                initCategories()
                initSubcategories()
            }
            MilinTheme {

                if (userId != 0)
                    Main(User(userId, username))
                else {
                    Login(User(userId, username)) { id, name ->
                        userId = id
                        username = name

                        // 保存用户信息
                        with(sharedPreferences.edit()) {
                            putInt("userId", userId)
                            putString("username", username)
                            apply()
                        }
                    }
                }
            }
        }
    }
}

