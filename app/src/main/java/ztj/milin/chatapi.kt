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
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ztj.milin.User

class ChatApi{

    private val client = HttpClient(CIO){
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
        val userRegistration = UserRegistration(username=user.name, password=password)
        val jsonString = json.encodeToString(userRegistration)
        val response: HttpResponse = client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/register") {
            body = TextContent(jsonString, ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()
        return if (response.status.value == 201) {
            // 假设响应体中包含用户的 id
            json.decodeFromString<Map<String, Int>>(responseBody)["success"] ?: 0
        } else {
            0
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun loginUser(user: User, password: String): Int {
        val json = Json { ignoreUnknownKeys = true }
        val userLogin = UserRegistration(username=user.name, password=password)
        val jsonString = json.encodeToString(userLogin)
        val response: HttpResponse = client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/login") {
            body = TextContent(jsonString, ContentType.Application.Json)
        }
        val responseBody = response.bodyAsText()

        return if (response.status.value == 200) {
            // 假设响应体中包含用户的 id
            json.decodeFromString<Map<String, Int>>(responseBody)["userId"] ?: 0
        } else {
            0
        }
    }



    suspend fun getMessages(user: User): List<Message> {
        val response: HttpResponse = client.get("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/messages") {
            parameter("username", user.name)
        }
        val responseBody = response.bodyAsText()

        val json = Json { ignoreUnknownKeys = true }
        val chatResponse = json.decodeFromString<ChatResponse>(responseBody)
        if (chatResponse.error != null) {
            return emptyList()
        }
        return chatResponse.chat
    }





    @OptIn(InternalAPI::class)
    suspend fun addMessage(senderUser: User, receiverUsername: String, message: String): Boolean {
        val json = Json { ignoreUnknownKeys = true }
        val mess = MessageRequest(message=message, senderUsername=senderUser.name, receiverUsername=receiverUsername)
        val jsonString = json.encodeToString(mess)
        val response: HttpResponse = client.post("https://server-tni-serverllication-jlocxabspm.cn-hangzhou.fcapp.run/message") {
            body = TextContent(jsonString, ContentType.Application.Json)
        }
        return response.status.value == 201
    }







}



@Serializable
data class ChatResponse(val chat: List<Message>, val error: String? = null)

@Serializable
data class UserRegistration(val username: String, val password: String)


@Serializable
data class Message(val id: Int, val sender: String?, val receiver: String?, val message: String)

@Serializable
data class MessageRequest(val message: String, val senderUsername: String, val receiverUsername: String)