package com.example.myapplication.utils

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class MyHandler : DefaultHandler(){

    private var nodeName = ""

    private lateinit var id : StringBuilder

    private lateinit var name : StringBuilder

    private lateinit var version : StringBuilder

    /**
     * 开始 XML 解析时调用
     */
    override fun startDocument() {
        // 初始化
        id = StringBuilder()
        name = StringBuilder()
        version = StringBuilder()
    }

    /**
     * 开始解析某个节点时调用
     * 每当开始解析某个节点时，此方法会调用。
     */
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        // 记录当前节点名，localName 参数记录着当前节点的名字
        nodeName = localName
        Log.d("TAG","uri is $uri")
        Log.d("TAG","localName is $localName")
        Log.d("TAG","qName is $qName")
        Log.d("TAG","attributes is $attributes")
    }

    /**
     * 获取节点中内容时调用
     * 在获取节点中的内容时，此方法可能会被调用多次，一些换行符也被当做内容解析出来。
     */
    override fun characters(ch: CharArray?, start: Int, length: Int) {
        // 根据当前节点名判断将内容添加到哪一个 StringBuilder 对象中
        when(nodeName){
            "id" -> id.append(ch,start,length)
            "name" -> name.append(ch,start,length)
            "version" -> version.append(ch,start,length)
        }
    }

    /**
     * 完成解析某个节点时调用
     */
    override fun endElement(uri: String?, localName: String?, qName: String?) {
        // 如果 app 节点已经解析完成
        if ("app" == localName){
            // 调用 trim()，避免可能包括回车或换行符。
            Log.e("TAG","id is ${id.toString().trim()}")
            Log.e("TAG","name is ${name.toString().trim()}")
            Log.e("TAG","version is ${version.toString().trim()}")
            // 最后要将 StringBuilder 清空
            id.setLength(0)
            name.setLength(0)
            version.setLength(0)
        }
    }

    /**
     * 完成整个 XML 解析时调用
     */
    override fun endDocument() {

    }











}