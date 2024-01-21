package ztj.milin

import ChatApi

val chatApi = ChatApi()

data class User(var id: Int, var name: String)


//置顶
fun topCategory(category: String) {
    categories.remove(category)
    categories.add(0, category)
}

// 新建类别
suspend fun addCategory(category: String): Boolean {
    if (categories.contains(category)) {
        return false
    }
    if (chatApi.addCategory(category)) {
        categories.add(category)
        return true
    }
    return false
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


val userList = arrayListOf(
    User(1, "User A"),
    User(2, "User B"),
    User(3, "User C"),
    // 添加更多用户
)

suspend fun addusertolist(user: String): Boolean {
    val id = chatApi.checkUser(user)
    return if (id == 0) {
        false
    } else {
        userList.add(User(id = id, name = user))
        true
    }
}


enum class Tab { Discover, Chat }

var categories = mutableListOf<String>()
suspend fun initCategories() {
    categories = chatApi.getCategories().toMutableList()
}


val subcategories = mutableMapOf<String, ArrayList<User>>()

// 添加一个子类别
fun addSubcategory(category: String, user: User) {
    if (subcategories.containsKey(category)) {
        subcategories[category]?.add(user)
    } else {
        subcategories[category] = arrayListOf(user)
    }
}

// 删除一个子类别
fun removeSubcategory(category: String, userId: Int) {
    subcategories[category]?.removeIf { it.id == userId }
}

// 修改一个子类别
fun updateSubcategory(category: String, userId: Int, newName: String) {
    subcategories[category]?.find { it.id == userId }?.name = newName
}

// 查询一个子类别
fun querySubcategory(category: String, userId: Int): User? {
    return subcategories[category]?.find { it.id == userId }
}
