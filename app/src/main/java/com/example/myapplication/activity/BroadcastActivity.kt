package com.example.myapplication.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity

/**
 * 在一个 IP 网络范围中，最大的 IP 地址是被保留作为广播地址来使用的。
 * 比如某个网络的 IP 范围是 192.168.0.XXX，子网掩码是 255.255.255.0，那么这个网络的广播地址就是 192.168.0.255。
 * 广播数据包会被发送到同一网络上的所有端口，这样该网络中的每台计算机都会收到这条广播。
 *
 * Android 中的广播主要可分为两种类型：
 * 标准广播（normal broadcasts）：是一种完全异步执行的广播，
 * 在广播发出之后，所有的 BroadcastReceiver 几乎会在同一时刻收到这条广播消息，因此它们之间没有任何先后顺序可言。
 * 这种广播的效率比较高，但同时也意味着它是无法被截断的。
 *
 * 有序广播（ordered broadcasts）：是一种同步执行的广播，
 * 在同一时刻只会有一个 BroadcastReceiver 能够接收到这条广播消息，当这个 BroadcastReceiver 中的逻辑执行完毕后，广播才会继续。
 * 所以此时的 BroadcastReceiver 是有先后顺序的，优先级高的就可以先收到广播消息，
 * 并且前面的还可以截断正在传递的广播，这样后面的 BroadcastReceiver 就无法收到广播消息了。
 *
 * Android 内置了很多系统级别的广播，比如开机完成，亮屏息屏，电量变化，系统时间变化等都会发出一条广播，
 * 可到如下路径查看完整的系统广播列表：
 * <Android SDK>/platforms/<任意 android api 版本>/data/broadcast_actions.txt
 * 发送广播是借助 Intent，而接收广播则使用 BroadcastReceiver，可自由注册自己感兴趣的广播来监听。
 */
class BroadcastActivity :BaseActivity(){

    lateinit var timeChangeReceiver: TimeChangeReceiver

    /**
     * 注册 BR 的方式一般有两种：静态注册（在 AndroidManifest.xml 中注册）和动态注册。
     * 创建 BR 的方式：只需新建一个类，让它继承自 BroadcastReceiver，并重写父类的 onReceive()。
     * 当有广播到来时，onReceive() 会得到执行。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)
        val intentFilter = IntentFilter()
        // 当系统时间发生变化时，每隔一分钟，系统发出的正是一条值为 android.intent.action.TIME_TICK 的广播
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver,intentFilter)
    }

    inner class TimeChangeReceiver:BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context,"Time is changed",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 动态注册的广播一定要取消注册才行
        unregisterReceiver(timeChangeReceiver)
    }

    companion object{
        fun actionStart(context: Context){

            val intent = Intent(context, BroadcastActivity::class.java)
            context.startActivity(intent)
        }
    }
}