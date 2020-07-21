package com.example.myapplication.service

import android.app.IntentService
import android.content.Intent
import android.util.Log

/**
 * 首先必须调用父类的构造函数并传入字符串，字符串可随意指定，只在调试时有用。
 *
 */
class MyIntentService : IntentService("MyIntentService") {

    /**
     * 实现这个抽象方法，位于子线程中运行，可以处理一些耗时逻辑。
     */
    override fun onHandleIntent(intent: Intent?) {
        // 打印当前线程的 id
        Log.d("TAG","Thread id is ${Thread.currentThread().name}")
    }

    /**
     * Service 会自动停止，并打印日志。
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG","onDestroy executed")
    }
}