package com.example.myapplication.multimedia

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initData()
    }

    private fun initData() {
        // 使用 Builder 构造器创建 Notification 对象。
        // 第二个参数是渠道 ID，需要和我们创建通知渠道时指定的渠道 ID 相匹配。
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 这里是英文字母 O，不是数字 0，尴尬。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 创建了一个 ID 为 normal 通知渠道。
            // 创建通知渠道的代码只在第一次执行时才会创建，当下次再执行创建代码时，
            // 系统会检测到该通知渠道已存在，因此不会重复创建，也并不会影响运行效率。
            val channel = NotificationChannel("normal","Normal",
                NotificationManager.IMPORTANCE_DEFAULT)
            // 创建一个重要等级的通知渠道。
            val channel2 = NotificationChannel("important","Important",
                NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            manager.createNotificationChannel(channel2)
        }

        // 注意在手机设置里,开启消息通知.
        btnSendNotice.setOnClickListener{
            // 表达想要启动 CameraAlbumActivity 的意图。
            // 可在 CameraAlbumActivity 中显示地调用 NotificationManager 的 cancel() 取消通知。
            val intent = Intent(this, CameraAlbumActivity::class.java)
            // PendingIntent 和 Intent 有些类似，它们都可以指明某一个"意图"，都可以用于启动 Activity、启动 Service 以及发送广播等。
            // 不同的是，Intent 倾向于立即执行某个动作。而 PendingIntent 倾向于在某个合适的时机执行某个动作，也可以简单地理解为延迟执行的 Intent。
            // 第二个参数一般用不到，传入 0 即可。
            // 第三个参数是一个 Intent 对象，可以通过这个对象构建出 PendingIntent 的"意图"。
            // 第四个参数用于确定 PendingIntent 的行为，一把传入 0 即可。（其它可选值：FLAG_ONE_SHOT,FLAG_CANCEL_CURRENT,FLAG_NO_CREATE,FLAG_UPDATE_CURRENT）
            val pi = PendingIntent.getActivity(this,0,intent,0)

            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle("This is content title")
//                .setContentText("This is content text")  // 使用 setStyle() 替代，展示长文字信息，或者是显示一张大图
                .setStyle(NotificationCompat.BigTextStyle()  // 展示长文字，在 iQOO Android 10 的手机上，默认折叠。
                    .bigText("Learn how to build notifications, send and sync data, and use voice actions, Get the official Android IDE and developer tools to build apps for Android."))
                .setSmallIcon(R.drawable.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.large_icon))
                .setContentIntent(pi)  // 添加点击效果
//                .setAutoCancel(true)   // 点击这个通知时，通知会自动取消。否则通知消息会一直留存在系统状态栏上。这里使用了另一种方式。
                .build()
            manager.notify(1,notification)

            // 将通知渠道的重要等级设置成了 "高"。关于弹出横幅，实际上在我的手机默认是关闭横幅通知的。
            val notification2 = NotificationCompat.Builder(this,"important")
                .setContentTitle("This is content title")
                .setStyle(NotificationCompat.BigPictureStyle()  // 展示大图，在 iQOO Android 10 的手机上，默认折叠。
                    .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.big_image)))
                .setSmallIcon(R.drawable.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.large_icon))
                .setContentIntent(pi)  // 添加点击效果
                .setAutoCancel(true)   // 点击这个通知时，通知会自动取消。否则通知消息会一直留存在系统状态栏上。
                .build()
            manager.notify(2,notification2)
        }
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }
}