package ztj.milin

data class User(val id: Int, val name: String)


val userList = listOf(
    User(1, "User A"),
    User(2, "User B"),
    User(3, "User C"),
    // 添加更多用户
)

enum class Tab { Discover, Chat }

val categories = arrayListOf("类别1", "类别2", "类别3")
val subcategories = mapOf(
    "类别1" to arrayListOf("子类别1A", "子类别1B", "子类别1C"),
    "类别2" to arrayListOf("子类别2A", "子类别2B"),
    "类别3" to arrayListOf("子类别3A")
)

