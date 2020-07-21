package com.example.myapplication.activity

import android.content.Context
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.kt.startActivity
import kotlinx.android.synthetic.main.activity_network.*
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class NetworkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        initView()
    }

    private fun sendRequestWithHttpUrlConnection(){
        // 开启线程发起网络请求
        thread {
            var connection:HttpURLConnection?=null
            try {
                val response = StringBuilder()
                val url = URL("https://www.baidu.com")
                connection = url.openConnection() as HttpURLConnection
                // 如果想要提交数据给服务器，主需要将 HTTP 的请求方法改成 POST，并在获取输入流之前把要提交的数据写出即可。
                // 注意，每条数据都要以键值对的形式存在，数据与数据之间用"&"符号隔开。
//                connection.requestMethod = "POST"
//                val output = DataOutputStream(connection.outputStream)
//                output.writeBytes("username=admin&password=123456")
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val input = connection.inputStream
                // 下面对获取到的输入流进行读取
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                showResponse(response.toString())
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                connection?.disconnect()
            }
        }
    }

    private fun showResponse(response: String) {
        // runOnUiThread() 其实就是对异步消息处理机制进行了一层封装，内部通过 Handler 实现。
        runOnUiThread{
            // 在这里进行 UI 操作，将结果显示到界面上。
            tvRequest.text = response
        }
    }

    private fun initView() {
        btnRequest.setOnClickListener{
            sendRequestWithHttpUrlConnection()
        }
    }

    companion object{
        fun actionStart(context: Context){
            // 使用泛型实化来启动 Activity
            startActivity<NetworkActivity>(context)
        }
    }
}