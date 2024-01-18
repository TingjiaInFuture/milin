package ztj.milin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ztj.milin.ui.theme.MilinTheme
import ztj.milin.client.main

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main()
        setContent {
            MilinTheme {
                var userId by remember { mutableStateOf(0) }
                var username by remember { mutableStateOf("NaN") }
                if (userId != 0)
                    Main(User(userId, username))
                else {
                    Login(User(userId, username)) { id, name ->
                        userId = id
                        username = name
                    }
                }
            }
        }
    }
}
