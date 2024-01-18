package ztj.milin.client

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

    fun main1(args: Array<String>) {
        try {
            var url = "https://milinapi.glitch.me/messages" // Glitch服务器的URL和端口号


            // 创建 URL 对象
            val obj = URL(url)

            // 打开连接
            val con = obj.openConnection() as HttpURLConnection

            // 设置请求方法为 GET
            con.requestMethod = "GET"

            // 获取服务器响应
            val responseCode = con.responseCode
            println("Response Code: $responseCode")

            // 读取服务器响应内容
            val `in` = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val response = StringBuffer()
            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()

            // 打印服务器响应内容
            println("Response Content: $response")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

