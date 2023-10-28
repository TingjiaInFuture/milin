package ztj.milin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(user: String) {
    var selectedTab by remember { mutableStateOf(Tab.Discover) }
    var selectedCategory by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                    label = { Text("觅林") },
                    selected = selectedTab == Tab.Discover,
                    onClick = { selectedTab = Tab.Discover }
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
                    label = { Text("对话") },
                    selected = selectedTab == Tab.Chat,
                    onClick = { selectedTab = Tab.Chat }
                )
            }
        },
        content = {
            when (selectedTab) {
                Tab.Discover -> MiLin(selectedCategory) { category -> selectedCategory = category }
                Tab.Chat -> Chat()
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Snackbar消息内容
                Snackbar(
                    modifier = Modifier
                        .padding(80.dp)
                        .paddingFromBaseline(top = 500.dp)
                ) {
                    Text(
                        text = "欢迎!$user",
                        color = Color.White,
                    )
                }
            }
        }

    )

    LaunchedEffect(1) {
        snackbarHostState.showSnackbar("欢迎!$selectedTab") // 显示Snackbar消息
        delay(2000) // 暂停3秒
        snackbarHostState.currentSnackbarData?.dismiss() // 取消Snackbar消息

    }

}