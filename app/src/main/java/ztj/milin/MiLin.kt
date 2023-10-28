package ztj.milin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MiLin(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6)),

        ) {
        items(categories.size) { index ->
            val category = categories[index]
            // 显示类别，点击时回调
            Text(
                text = category,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Green)

                    .clickable { onCategorySelected(category) }
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
}
