package ztj.milin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MiLin(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onCategoryAdded: (String) -> Unit,
    onCategoryRemoved: (String) -> Unit
) {
    // 用于控制上下文菜单的显示
    var showContextMenu by remember { mutableStateOf(false) }
    var contextMenuCategory by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFADD8E6))) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFADD8E6)),

            ) {
            items(categories.size) { index ->
                val category = categories[index]
                // 显示类别，点击时回调
                Text(
                    text = category,
                    color =  Color(0xFF056B05),
                    modifier = Modifier
                        .padding(16.dp)
                        .combinedClickable(
                            onClick = {
                                onCategorySelected(category)
                            },
                            onLongClick = {
                                showContextMenu = true
                                contextMenuCategory = category
                            }
                        )
                )

                // 如果类别被选中，显示子类
                if (category == selectedCategory) {
                    subcategories[category]?.forEachIndexed { subcategoryIndex, subcategory ->
                        Text(
                            text = subcategory,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        // 显示一个悬浮的 "+" 按钮
        FloatingActionButton(
            onClick = { /* 在这里处理点击事件，例如弹出一个对话框让用户输入新的类别名称 */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(width = 100.dp, height = 100.dp)
//                .height(100.dp).width(200.dp)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "添加类别")
//            Text(
//                text = "我在这",
//                modifier = Modifier.padding(16.dp)
//            )
        }

        // 如果需要显示上下文菜单，那么显示它
        if (showContextMenu) {
            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = { /* 在这里处理置顶操作 */ },
                    text = { Text("置顶") }
                )
                DropdownMenuItem(
                    onClick = {
                        // 在这里处理删除操作
                        onCategoryRemoved(contextMenuCategory)
                        showContextMenu = false
                    },
                    text = { Text("删除") }
                )

            }
        }
    }
}
