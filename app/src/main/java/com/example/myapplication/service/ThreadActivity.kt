package com.example.myapplication.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_thread.*
import kotlin.concurrent.thread


class ThreadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)

        initView()
        initData()
    }

    /**
     * 定义一个整型变量，用来表示更新 TextView 这个动作
     */
    val updateText = 1

    private fun initView() {
        btnChangeText.setOnClickListener{
            val msg = Message()
            msg.what = updateText
            // 将 Message 对象发送出去
            handler.sendMessage(msg)
        }
    }

    /**
     * 新增一个 Handler 对象，并重写父类的 handleMessage()
     */
    val handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // 进行 UI 操作
            when(msg.what){
                updateText -> tvText.text = "Nice to meet you"
            }
        }
    }

    private fun initData() {
        // 启动线程
        MyThread().start()

        val myThread = MyThread2()
        // Thread 构造函数接收一个 Runnable 参数
        Thread(myThread).start()

        // 如果不想专门定义一个类去实现 Runnable 接口，
        // 可以使用 Lambda 的方式，这种写法更为常见。
        Thread{
            // 编写具体的逻辑
            Log.e("TAG","匿名 Thread is start。")
        }.start()

        // Kotlin 提供了一种更加简单的开启线程的方式，
        // 这里的 Thread 是一个 Kotlin 内置的顶层函数，
        // 只需要在 Lambda 表达式中编写具体的逻辑即可。
        thread {
            // 编写具体的逻辑
            Log.e("TAG","Kotlin Thread is start。")
        }
    }

    /**
     * 创建类继承自 Thread，并重新父类的 run()。
     */
    class MyThread:Thread(){
        override fun run() {
            super.run()
            // 编写的逻辑
            Log.e("TAG","类继承 Thread is start。")
        }
    }

    /**
     * 更加推荐使用实现 Runnable 接口的方式来定义一个线程，
     * 因为继承的方式耦合性有点高。
     */
    class MyThread2:Runnable{
        override fun run() {
            // 编写的逻辑
            Log.e("TAG","实现接口 Thread is start。")
        }
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, ThreadActivity::class.java)
            context.startActivity(intent)
        }
    }
}

