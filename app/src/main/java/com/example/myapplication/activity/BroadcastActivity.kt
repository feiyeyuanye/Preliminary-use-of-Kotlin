package com.example.myapplication.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_broadcast.*

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

        // 发送自定义广播
        btnSendBR.setOnClickListener{
            // 静态注册
            val intent = Intent("com.example.myapplication.broadcast.MY_BROADCAST")
            // Android 8.0 后，静态注册的 BroadcastReceiver 是无法接收隐式广播的，
            // 而默认情况下发出的自定义广播恰恰都是隐式广播。
            // 因此这里调用 setPackage() 指定这条广播是发送给哪个应用程序的，从而让它变成一条显式广播。
            // packageName 是 getPackageName() 的语法糖写法，用于获取当前应用的包名。
            intent.setPackage(packageName)
            // 因为是通过 intent 发送的，还可以如 Activity 一般携带一些数据。
            // 发送标准广播（无序广播）
//            sendBroadcast(intent)
            // 发送有序广播,第二个参数是一个与权限相关的字符串，传入 null 即可。
            // <intent-filter android:priority="100"> 指定优先级，优先级高的先收到广播
            // onReceive() 中调用 abortBroadcast() 可以拦截广播。
            sendOrderedBroadcast(intent,null)
        }

        // 动态注册
        val intentFilter = IntentFilter()
        // 当系统时间发生变化时，每隔一分钟，系统发出的正是一条值为 android.intent.action.TIME_TICK 的广播
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver,intentFilter)

        // 实现强制下线功能
        btnForce.setOnClickListener{
            // 这条广播就是用于通知程序强制下线的。
            // 强制下线的逻辑并不写在这里，而应该写在接收这条广播的 BroadcastReceiver 里，
            // 这样强制下线的功能就不会依附于任何界面了。
            // 由于 BR 中需要弹框来阻塞用户操作，而静态的 BR 是没办法在 onReceive() 里弹出对话框这样的 UI 控件的，
            // 但又不能在每个 Activity 中都注册一个动态的 BR，所以只需要在 BaseActivity() 动态注册一个 BR 即可。
            val intent = Intent("com.example.broadcast.FORCE_OFFLINE")
            sendBroadcast(intent)
        }
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