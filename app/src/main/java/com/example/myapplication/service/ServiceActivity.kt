package com.example.myapplication.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity:BaseActivity() {

    lateinit var downloadBinder: MyService.DownloadBinder

    /**
     * 创建 ServiceConnection 的匿名类实现
     */
    private val connection = object :ServiceConnection{
        /**
         * 在 Activity 和 Service 成功绑定时调用
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // 通过向下转型得到了 DownloadBinder 实例
            downloadBinder = service as MyService.DownloadBinder
            downloadBinder.startDownload()
            downloadBinder.getProgress()
        }

        /**
         * 只有在 Service 的创建进程崩溃或者被杀掉时才会调用，这个方法不太常用。
         */
        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        initView()
    }

    private fun initView() {
        btnStartService.setOnClickListener{
            val intent = Intent(this, MyService::class.java)
            // startService 和 stopService 都是定义在 Context 类中的，所以可在 Activity 里直接调用。
            startService(intent)  // 启动 Service
        }
        btnStopService.setOnClickListener{
            val intent = Intent(this, MyService::class.java)
            // Service 也可以自我停止运行，只需要在 Service 内部调用 stopSelf()。
            stopService(intent)  // 启动 Service
        }
        btnBindService.setOnClickListener{
            val intent = Intent(this, MyService::class.java)
            // 绑定 Service，
            // 第三个参数是一个标志位，这里表示在 Activity 和 Service 进行绑定后自动创建 Service。
            // 这会使得 MyService 中的 onCreate() 得到执行，但 onStartCommand() 不会执行。
            bindService(intent,connection,Context.BIND_AUTO_CREATE)

            // 打印结果：
            // D/TAG: onCreate
            // D/TAG: startDownload executed
            // D/TAG: getProgress executed
        }
        btnUnBindService.setOnClickListener{
            val intent = Intent(this, MyService::class.java)
            // 解绑 Service
            unbindService(connection)

            // 打印结果：
            // D/TAG: onDestroy
        }
        btnStartIntent.setOnClickListener{
            // 打印主线程的 id
            Log.d("TAG","Thread id is ${Thread.currentThread().name}")
            val intent = Intent(this,MyIntentService::class.java)
            startService(intent)

            // 打印结果：
            // D/TAG: Thread id is main
            // D/TAG: Thread id is IntentService[MyIntentService]
            // D/TAG: onDestroy executed
        }
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, ServiceActivity::class.java)
            context.startActivity(intent)
        }
    }
}