package com.example.myapplication.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * 动态注册必须在程序启动之后才能接收广播，因为注册的逻辑是写在 onCreate() 中的。
 * Android 8.0 系统后，所有隐式广播都不允许使用静态注册的方式来接收了，
 * 隐式广播指的是那些没有具体指定发送给哪个应用程序的广播，大多系统广播属于隐式广播，
 * 但是少数特殊的系统广播目前仍然允许使用静态注册的方式接收，这些特殊的系统广播列表详见：
 * https://developer.android.google.cn/guide/components/broadcast-exceptions.html
 *
 * 这里采用静态注册的方式，实现开机启动。
 */
class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // BroadcastReceiver 中是不允许开启线程的，所以要避免在这里添加过多的逻辑或者是耗时操作。
        Toast.makeText(context,"Boot Complete",Toast.LENGTH_LONG).show()
        Log.d("TAG","Boot Complete")
    }
}
