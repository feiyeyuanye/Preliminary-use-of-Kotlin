package com.example.myapplication.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.activity.ServiceActivity
import kotlin.concurrent.thread

/**
 * 手动创建的要记得在文件内注册
 * 任何一个 Service 在整个应用程序范围内都是通用的。
 */
class MyService : Service() {

    private val mBinder = DownloadBinder()

    /**
     * 提供下载功能，创建一个专门的 Binder 对象来对下载功能进行管理。
     */
    class DownloadBinder: Binder(){

        fun startDownload(){
            // 下载
            Log.d("TAG","startDownload executed")
        }

        fun getProgress():Int{
            // 查看进度
            Log.d("TAG","getProgress executed")
            return 0
        }
    }

    /**
     * Service 中唯一的抽象方法，所以必须在子类里实现。
     */
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    /**
     * Service 第一次创建时调用
     */
    override fun onCreate() {
        super.onCreate()
        Log.d("TAG","onCreate")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("my_service","前台 Service 通知",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this,ServiceActivity::class.java)
        val pi = PendingIntent.getActivity(this,0,intent,0)
        val notification = NotificationCompat.Builder(this,"my_service")
            .setContentTitle("This is content title")
            .setContentText("This is content text")
            .setSmallIcon(R.drawable.small_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources,
                R.drawable.large_icon
            ))
            .setContentIntent(pi)
            .build()
        // 构建 Notification 对象后并没使用 NotificationManager 将通知显示出来，
        // 而是调用了 startForeground()，让 MyService 变成了一个前台 Service，并在系统状态栏显示出来。
        // 第一个参数是通知的 id，类似于 notify() 的第一个参数，
        // 第二个参数是构建的 Notification 对象，
        startForeground(1,notification)
    }

    /**
     * 每次 Service 启动时调用，立刻执行某个动作。
     *
     * 日志打印：
     * D/TAG: onCreate
     * D/TAG: onStartCommand
     * D/TAG: onStartCommand
     * D/TAG: onStartCommand
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAG","onStartCommand")
        // Service 中的代码都是默认运行在主线程中，不适合执行耗时逻辑，需要使用多线程编程技术。
        thread {
            // 处理具体逻辑

            // 如果需要在执行完毕后自动停止的功能
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Service 销毁时调用，回收不再使用的资源。
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG","onDestroy")

    }

}
