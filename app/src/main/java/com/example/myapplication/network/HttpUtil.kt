package com.example.myapplication.network

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object HttpUtil{

    /**
     * @param callback OkHttp 库中自带的回调接口
     *
     */
    fun sendOkHttpRequest(address: String,callback:okhttp3.Callback){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(address)
            .build()
        // enqueue() 异步执行
        client.newCall(request).enqueue(callback)
    }

    fun sendHttpRequest(address: String,listener: HttpCallbackListener){
        // 子线程中是无法通过 return 语句返回数据的
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL(address)
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                // 回调 onFinish() 方法
                listener.onFinish(response.toString())
            }catch (e:Exception){
                e.printStackTrace()
                // 回调 onError() 方法
                listener.onError(e)
            }finally {
                connection?.disconnect()
            }
        }
    }
}