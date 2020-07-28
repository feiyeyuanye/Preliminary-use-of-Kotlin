package com.example.myapplication.ui.bean

class ChatMsgBean(val content:String,val type:Int) {

    companion object{
        // const 关键字定义常量
        // 只有 companion object 或顶层方法中才能使用 const 关键字
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
    }

}