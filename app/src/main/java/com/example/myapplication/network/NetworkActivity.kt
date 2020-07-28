package com.example.myapplication.network

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.bean.AppBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_network.*
import okhttp3.*
import org.json.JSONArray
import org.xml.sax.InputSource
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.SAXParserFactory
import kotlin.concurrent.thread



class  NetworkActivity : BaseActivity() {

    private val tag = "TAG_NetworkActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        initView()
    }

    private fun initView() {
        btnRequest.setOnClickListener{
//            sendRequestWithHttpUrlConnection()
            val address = "https://jianghouren.com/"
            // 回调接口在子线程中运行，不可以执行 UI 操作，除非借助 runOnUiThread()。
            HttpUtil.sendHttpRequest(address, object :
                HttpCallbackListener {
                override fun onFinish(response: String) {
                    // 得到服务器返回的具体内容
                    showResponse(response)
                }

                override fun onError(e: Exception) {
                    // 在这里对异常情况进行处理
                }
            })
        }

        btnOkHttp.setOnClickListener{
//            sendRequestWithOkHttp()
            val address = "https://jianghouren.com/"
            // 回调接口在子线程中运行，不可以执行 UI 操作，除非借助 runOnUiThread()。
            HttpUtil.sendOkHttpRequest(address,object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    // 得到服务器返回的具体内容
                    val responseData = response.body?.string()
                    if (responseData!=null){
                        showResponse(responseData)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    // 在这里对异常情况进行处理
                }
            })
        }

        btnPull.setOnClickListener{
            pullXMLWithOkHttp()
        }
        btnSAX.setOnClickListener{
            saxXMLWithOkHttp()
        }
        btnJSONObject.setOnClickListener{
            jsonObjectWithOkHttp()
        }
        btnGSON.setOnClickListener{
            gsonWithOkHttp()
        }
    }


    private fun sendRequestWithHttpUrlConnection(){
        // 开启线程发起网络请求
        thread {
            var connection: HttpURLConnection?=null
            try {
                val response = StringBuilder()
                val url = URL("https://jianghouren.com/")
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

    private fun sendRequestWithOkHttp() {
        // 记录一下，百度首页的显示结果会与上面不一致，
        // 请求返回 location.replace(location.href.replace("https://","http://");
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://jianghouren.com/")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    showResponse(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


    private fun pullXMLWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                        // 指定访问的服务器地址是计算机本机
                        // 127.0.0.1 指的是计算机
                        // 10.0.2.2 指的是模拟器，对于模拟器来说就是计算机本机的 IP 地址
//                    .url("http://127.0.0.1/get_data.xml")
                    .url("http://10.0.2.2/get_data.xml")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    // 查看服务器返回的数据
                    showResponse(responseData)
                    // 解析服务器返回的数据
                    parseXMLWithPull(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseXMLWithPull(xmlData: String) {
        try {
            // 创建一个 XmlPullParserFactory 实例
            val factory = XmlPullParserFactory.newInstance()
            // 借助 XmlPullParserFactory 实例得到 XmlPullParser 对象
            val xmlPullParser = factory.newPullParser()
            // 将服务器返回的 XML 数据设置进去
            xmlPullParser.setInput(StringReader(xmlData))
            // 开始解析
            // 通过 getEventType() 可以得到当前的解析事件
            var eventType = xmlPullParser.eventType
            var id = ""
            var name = ""
            var version = ""
            // 在 while 循环中不断解析
            while (eventType != XmlPullParser.END_DOCUMENT){
                // 得到当前节点的名字
                val nodeName = xmlPullParser.name
                when(eventType){
                    // 开始解析某个节点
                    XmlPullParser.START_TAG -> {
                        // 如果发现节点名等于 id、name 或 version，就调用 nextText() 获取节点内具体的内容。
                        when(nodeName){
                            "id" -> id = xmlPullParser.nextText()
                            "name" -> name = xmlPullParser.nextText()
                            "version" -> version = xmlPullParser.nextText()
                        }
                    }
                    // 完成解析某个节点
                    XmlPullParser.END_TAG -> {
                        // 每当解析完一个 app 节点，就将获取到的内容打印出来
                        if ("app" == nodeName){
                            Log.e(tag,"id is $id")
                            Log.e(tag,"name is $name")
                            Log.e(tag,"version is $version")
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun saxXMLWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2/get_data.xml")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    // 查看服务器返回的数据
                    showResponse(responseData)
                    // 解析服务器返回的数据
                    parseXMLWithSAX(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseXMLWithSAX(xmlData: String) {
        try {
            // 创建了一个 SAXParserFactory 对象
            val factory = SAXParserFactory.newInstance()
            // 获取 XmlReader 对象
            val xmlReader = factory.newSAXParser().xmlReader
            val handler = MyHandler()
            // 将 MyHandler 的实例设置到 XMLReader 中
            xmlReader.contentHandler = handler
            // 开始执行解析
            xmlReader.parse(InputSource(StringReader(xmlData)))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun gsonWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2/get_data.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    // 查看服务器返回的数据
                    showResponse(responseData)
                    // 解析服务器返回的数据
                    parseJSONWithGSON(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseJSONWithGSON(jsonData: String) {
        val gson = Gson()
        val typeOf = object: TypeToken<List<AppBean>>(){}.type
        val appList = gson.fromJson<List<AppBean>>(jsonData, typeOf)
        for (app in appList){
            Log.d(tag,"id is ${app.id}")
            Log.d(tag,"name is ${app.name}")
            Log.d(tag,"version is ${app.version}")
        }
    }

    private fun jsonObjectWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2/get_data.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    // 查看服务器返回的数据
                    showResponse(responseData)
                    // 解析服务器返回的数据
                    parseJSONWithJSONObject(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseJSONWithJSONObject(jsonData: String) {
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val name = jsonObject.getString("name")
                val version = jsonObject.getString("version")
                Log.d(tag,"id is $id")
                Log.d(tag,"name is $name")
                Log.d(tag,"version is $version")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun showResponse(response: String) {
        // runOnUiThread() 其实就是对异步消息处理机制进行了一层封装，内部通过 Handler 实现。
        runOnUiThread{
            // 在这里进行 UI 操作，将结果显示到界面上。
            tvRequest.text = response
            // 从结果看，相比较 HttpUrlConnection，OkHttp 的请求结果显示有排版。
        }
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, NetworkActivity::class.java)
            context.startActivity(intent)
        }
    }
}