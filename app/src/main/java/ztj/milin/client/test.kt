package ztj.milin.client

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class MessageRequest1(val user: String?, val message: String)

fun main0() {
    val json = Json { ignoreUnknownKeys = true }
    val mess = MessageRequest1("user", "message")
    val jsonString = json.encodeToString(mess)
    println(jsonString)
}
//        val intent = Intent(this, FullscreenActivity::class.java)
//        startActivity(intent)