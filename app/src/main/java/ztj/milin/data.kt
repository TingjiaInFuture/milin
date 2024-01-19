package ztj.milin



data class User(var id: Int, var name: String)


//置顶
fun topCategory(category: String) {
    categories.remove(category)
    categories.add(0, category)
}
// 新建类别
fun addCategory(category: String) {
    categories.add(category)
}

// 删除类别
fun removeCategory(category: String) {
    categories.remove(category)
    subcategories.remove(category) // 同时删除子类别
}

// 更新类别
fun updateCategory(oldCategory: String, newCategory: String) {
    val index = categories.indexOf(oldCategory)
    if (index != -1) {
        // 如果旧类别存在，替换为新类别
        categories[index] = newCategory

        // 同时更新子类别
        subcategories[newCategory] = subcategories.remove(oldCategory) ?: arrayListOf()
    } else {
        println("类别 $oldCategory 不存在.")
    }
}


val userList = listOf(
    User(1, "User A"),
    User(2, "User B"),
    User(3, "User C"),
    // 添加更多用户
)

enum class Tab { Discover, Chat }

val categories = arrayListOf("类别1", "类别2", "类别3")

val subcategories = mutableMapOf(
    "类别1" to arrayListOf("子类别1A", "子类别1B", "子类别1C"),
    "类别2" to arrayListOf("子类别2A", "子类别2B"),
    "类别3" to arrayListOf("子类别3A")
)


