package ztj.milin.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.util.InternalAPI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ztj.milin.User


class clienttest {

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 10000 // 0 to disable, or a millisecond value to fit your needs
        }
        defaultRequest {
            header("admin_key", "123456")
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun registerUser(user: User, password: String): Int {
        val json = Json { ignoreUnknownKeys = true }
        val userRegistration = UserRegistration(username = user.name, password = password)
        val jsonString = json.encodeToString(userRegistration)
        val response: HttpResponse =
            client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/register") {
                body = TextContent(jsonString, ContentType.Application.Json)
            }
        val responseBody = response.bodyAsText()
        println("registerUser response: $responseBody")
        return if (response.status.value == 201) {

            json.decodeFromString<Map<String, Int>>(responseBody)["success"] ?: 0
        } else {
            0
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun loginUser(user: User, password: String): Int {
        val json = Json { ignoreUnknownKeys = true }
        val userLogin = UserRegistration(username = user.name, password = password)
        val jsonString = json.encodeToString(userLogin)
        val response: HttpResponse =
            client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/login") {
                body = TextContent(jsonString, ContentType.Application.Json)
            }
        val responseBody = response.bodyAsText()
        println("loginUser response: $responseBody")
        return if (response.status.value == 200) {

            json.decodeFromString<Map<String, Int>>(responseBody)["userId"] ?: 0
        } else {
            0
        }
    }


    suspend fun getMessages(user: User): List<Message> {
        val response: HttpResponse =
            client.get("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/messages") {
                parameter("username", user.name)
            }
        val responseBody = response.bodyAsText()
        println("getMessages response: $responseBody")
        val json = Json { ignoreUnknownKeys = true }
        val chatResponse = json.decodeFromString<ChatResponse>(responseBody)
        if (chatResponse.error != null) {
            println("Error: ${chatResponse.error}")
            return emptyList()
        }
        return chatResponse.chat
    }


    @OptIn(InternalAPI::class)
    suspend fun addMessage(senderUser: User, receiverUsername: String, message: String): Boolean {
        val json = Json { ignoreUnknownKeys = true }
        val mess = MessageRequest(
            message = message,
            senderUsername = senderUser.name,
            receiverUsername = receiverUsername
        )
        val jsonString = json.encodeToString(mess)
        val response: HttpResponse =
            client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/message") {
                body = TextContent(jsonString, ContentType.Application.Json)
            }
        val responseBody = response.bodyAsText()
        println("addMessage response: $responseBody")
        return response.status.value == 201
    }


    suspend fun checkUser(username: String): Int {
        val response: HttpResponse =
            client.get("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/checkUsername") {
                parameter("username", username)
            }
        val responseBody = response.bodyAsText()
        println("checkUser response: $responseBody")
        val json = Json { ignoreUnknownKeys = true }
        return if (response.status.value == 200) {
            json.decodeFromString<Map<String, Int>>(responseBody)["userId"] ?: 0
        } else {
            0
        }
    }

    suspend fun getCategories(): List<String> {
        val response: HttpResponse =
            client.get("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/categories")
        val responseBody = response.bodyAsText()
        println("getCategories response: $responseBody")
        val json = Json { ignoreUnknownKeys = true }
        return if (response.status.value == 200) {

            json.decodeFromString<List<String>>(responseBody)
        } else {
            emptyList()
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun addCategory(category: String): Boolean {
        val json = Json { ignoreUnknownKeys = true }
        val jsonString = json.encodeToString(mapOf("category" to category))
        val response: HttpResponse =
            client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/categories") {
                body = TextContent(jsonString, ContentType.Application.Json)
            }
        val responseBody = response.bodyAsText()
        println("addCategory response: $responseBody")
        return response.status.value == 201
    }


    // 获取给定主类的子类
    suspend fun getSubcategories(category: String): List<String> {
        val response: HttpResponse =
            client.get("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/subcategories") {
                parameter("category", category)
            }
        val responseBody = response.bodyAsText()
        println("getSubcategories response: $responseBody")
        val json = Json { ignoreUnknownKeys = true }
        return if (response.status.value == 200) {
            json.decodeFromString<List<String>>(responseBody)
        } else {
            emptyList()
        }
    }

    // 添加新的子类
    @OptIn(InternalAPI::class)
    suspend fun addSubcategory(category: String, user: String): Boolean {
        val json = Json { ignoreUnknownKeys = true }
        val jsonString = json.encodeToString(mapOf("category" to category, "user" to user))
        val response: HttpResponse =
            client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/subcategories") {
                body = TextContent(jsonString, ContentType.Application.Json)
            }
        val responseBody = response.bodyAsText()
        println("addSubcategory response: $responseBody")
        return response.status.value == 201
    }


}


fun main() {
    val clientTest = clienttest()
    runBlocking {
        clientTest.registerUser(User(id = 1, name = "群聊1"), "123456")
//        clientTest.checkUser("User B")
        clientTest.registerUser(User(id = 2, name = "群聊2"), "123456")
//        clientTest.checkUser("User B")
        clientTest.registerUser(User(id = 3, name = "群聊3"), "123456")
        clientTest.registerUser(User(id = 4, name = "网络用户A"), "123456")

//        clientTest.addCategory("示例1")
//        clientTest.getCategories()
//        clientTest.addSubcategory("示例1","网络用户A")
//        clientTest.addSubcategory("示例1","B")
//        clientTest.getSubcategories("示例1")
//        clientTest.addCategory("示例2")
    }
//    val senderUser = User(id=9,name="User A")
//    val receiverUser = User(id=10,name="User B")
//    val UserC = User(id=10,name="User C")
//    val user1 = User(id=125,name="Usr 51")
//    val message = "Hello, world!"
//    runBlocking {
//        val a=clientTest.registerUser(senderUser,"123456")
//        val b=clientTest.registerUser(receiverUser,"123456")
//        clientTest.registerUser(UserC,"123456")
////        val messages = clientTest.addMessage(senderUser, receiverUser.name, message)
//        println("id: $a")
//        println("id: $b")
////        println(messages)
//        println(clientTest.loginUser(user1,"123456"))
//        clientTest.registerUser(user1,"123456")
//        println(clientTest.loginUser(user1,"123456"))
//    }
//    runBlocking {
//        val messages = clientTest.getMessages(senderUser)
//        messages.forEach { println(it) }
//        val messages1 = clientTest.getMessages(receiverUser)
//        messages1.forEach { println(it) }
//        val messages2 = clientTest.getMessages(UserC)
//        messages2.forEach { println(it) }
//    }
}


@Serializable
data class ChatResponse(val chat: List<Message>, val error: String? = null)

@Serializable
data class UserRegistration(val username: String, val password: String)


@Serializable
data class Message(val id: Int, val sender: String?, val receiver: String?, val message: String)

@Serializable
data class MessageRequest(
    val message: String,
    val senderUsername: String,
    val receiverUsername: String
)
