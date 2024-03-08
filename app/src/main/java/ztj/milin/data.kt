package ztj.milin

import Api

val api = Api()

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
    if (api.addCategory(category)) {
        categories.add(category)
        return true
    }
    return false
}

// 删除类别
suspend fun removeCategory(category: String): Boolean {
    if (!categories.contains(category)) {
        return false
    }
    if (api.deleteCategory(category)) {
        categories.remove(category)
        if (deleteAllSubcategories(category)) {
            subcategories.remove(category)
            return true
        }
    }
    return false


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
    User(1, "群聊1"),
    User(2, "群聊2"),
    User(3, "群聊3"),
    // 添加更多用户
)

val newuserList = ArrayList<User>()

suspend fun addusertolist(user: String): Boolean {
    val id = api.checkUser(user)
    return if (id == 0) {
        false
    } else {
        userList.add(User(id = id, name = user))
        true
    }
}


enum class Tab { Discover, Chat, Explore }

var categories = mutableListOf<String>()
suspend fun initCategories() {
    categories = api.getCategories().toMutableList()
}


val subcategories = mutableMapOf<String, ArrayList<User>>()

suspend fun initSubcategories() {
    for (category in categories) {
        val subcategoryNames = api.getSubcategories(category)
        val subcategoryUsers = subcategoryNames.mapNotNull { name ->
            val userId = api.checkUser(name)
            if (userId != 0) User(id = userId, name = name) else null
        }
        subcategories[category] = ArrayList(subcategoryUsers)
    }
}


// 添加一个子类别
suspend fun addSubcategory(category: String, user: User): Boolean {
    if (api.addSubcategory(category, user.name)) {

        if (subcategories.containsKey(category)) {
            subcategories[category]?.add(user)
        } else {
            subcategories[category] = arrayListOf(user)
        }
        return true
    }
    return false
}

// 删除一个子类别
suspend fun removeSubcategory(category: String, user: String): Boolean {
    if (api.deleteSubcategory(category, user)) {
        subcategories[category]?.removeIf { it.name == user }
        return true
    }
    return false
}

// 修改一个子类别
fun updateSubcategory(category: String, userId: Int, newName: String) {
    subcategories[category]?.find { it.id == userId }?.name = newName
}

// 查询一个子类别
fun querySubcategory(category: String, userId: Int): User? {
    return subcategories[category]?.find { it.id == userId }
}

suspend fun deleteAllSubcategories(category: String): Boolean {
    // 首先获取该类下的所有子类
    val subcategories = subcategories[category] ?: return true
    var success = true

    // 遍历这些子类并逐个删除
    for (user in subcategories) {
        val result = api.deleteSubcategory(category, user.name)
        if (!result) {
            success = false
        }
    }

    return success
}
