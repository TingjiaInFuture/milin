package ztj.milin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(user: User) {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.isStatusBarVisible = false
////        // 设置状态栏颜色
////        systemUiController.setSystemBarsColor(
////            color = Color(0xFFADD8E6)
////        )
//    }

    var selectedTab by remember { mutableStateOf(Tab.Discover) }
    var selectedCategory by remember { mutableStateOf("") }//状态提升
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),


        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Default.Face, contentDescription = null) },
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
                NavigationBarItem( // 新增的 Tab
                    icon = { Icon(imageVector = Icons.Default.List, contentDescription = null) },
                    label = { Text("发现") },
                    selected = selectedTab == Tab.Explore,
                    onClick = { selectedTab = Tab.Explore }
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {

                when (selectedTab) {
                    Tab.Discover -> MiLin(
                        user = user,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category -> selectedCategory = category }
                    )

                    Tab.Chat -> Chat(user)
                    Tab.Explore -> Explore(user)
                }

//            SnackbarHost(
//                hostState = snackbarHostState,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // Snackbar消息内容
//                Snackbar(
//                    modifier = Modifier
//                        .padding(80.dp)
//                        .paddingFromBaseline(top = 500.dp)
//                ) {
//                    Text(
//                        text = "欢迎!$user",
//                        color = Color.White,
//                    )
//                }
//            }

            }
        }
    )

//    LaunchedEffect(key1 = selectedTab) {
//        snackbarHostState.showSnackbar(
//            message = "Welcome to $selectedTab",
//            duration = SnackbarDuration.Short
//        )
//    }
}
