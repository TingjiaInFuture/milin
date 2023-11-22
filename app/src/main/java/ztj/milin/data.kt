package ztj.milin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class User(val id: Int, val name: String)
 var auth: FirebaseAuth = Firebase.auth
// 新建类别
fun addCategory(category: String) {
    categories.add(category)
}

// 删除类别
fun removeCategory(category: String) {
    categories.remove(category)
    subcategories.remove(category) // 同时删除子类别
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


