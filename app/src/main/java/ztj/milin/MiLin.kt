package ztj.milin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MiLin(
    user: User,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var showContextMenu by remember { mutableStateOf(false) }
    var contextMenuCategory by remember { mutableStateOf("") }
    var showadd by remember { mutableStateOf("") }
    var i by remember { mutableIntStateOf(0) }
    var addc by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(categories.size) { index ->
                val category = categories[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 显示类别，点击时回调
                    Text(
                        text = category,
//                        color = Color(0xFF056B05),
                        modifier = Modifier.combinedClickable(onClick = {
                            showadd = category
                            onCategorySelected(category)

                        }, onLongClick = {
                            showContextMenu = true
                            contextMenuCategory = category
                        })
                    )
                    if (showadd == category) {
                        IconButton(onClick = {

                            CoroutineScope(Dispatchers.Main).launch {
                                if (querySubcategory(selectedCategory, user.id) == null) {
                                    addSubcategory(selectedCategory, user)
                                    onCategorySelected("$i")
                                    i++
                                } else {
                                    if (!removeSubcategory(selectedCategory, user.name))
                                        snackbarHostState.showSnackbar("删除失败：服务器错误")
                                    onCategorySelected("$i")
                                    i++
                                }

                                showadd = ""
                            }
                        }) {
                            if (querySubcategory(selectedCategory, user.id) == null) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "加入候选",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color(0xFFE78E06)
                                )
                            } else {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "退出候选",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color(0xFFE78E06)
                                )
                            }
                        }
                    }
                }

                // 如果类别被选中，显示子类
                if (category == selectedCategory) {
                    subcategories[category]?.forEachIndexed { subcategoryIndex, subcategory ->
                        Text(
                            text = subcategory.name, modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
        if (addc) {
            var newcategory by remember { mutableStateOf("") }
            Dialog(onDismissRequest = {
                CoroutineScope(Dispatchers.Main).launch {
                    addc = false
                    if (newcategory == "")
                        snackbarHostState.showSnackbar("类别名为空")
                    else if (!addCategory(newcategory))
                        snackbarHostState.showSnackbar("类别已存在或服务器错误")
                    onCategorySelected("$i")
                    i++
                }
            }) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                ) {
                    TextField(
                        value = newcategory,
                        onValueChange = { newcategory = it },
                        label = { Text("添加新项：") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }

        }
        // 显示一个悬浮的 "+" 按钮
        FloatingActionButton(
            onClick = {
                addc = true
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(100.dp)
                .padding(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "添加类别")

        }

        // 如果需要显示上下文菜单，那么显示它
        if (showContextMenu) {
            DropdownMenu(expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false }) {
                DropdownMenuItem(onClick = {
                    topCategory(contextMenuCategory)
                    onCategorySelected("$i")
                    i++
                    showContextMenu = false
                }, text = { Text("置顶") })
                DropdownMenuItem(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (!removeCategory(contextMenuCategory))
                            snackbarHostState.showSnackbar("服务器错误")
                        onCategorySelected("$i")
                        i++
                    }
                    showContextMenu = false
                }, text = { Text("删除") })


            }
        }
    }
    SnackbarHost(hostState = snackbarHostState)
}

