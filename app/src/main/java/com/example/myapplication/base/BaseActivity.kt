package com.example.myapplication.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.activity.MainActivity
import com.example.myapplication.utils.ActivityCollector

open class BaseActivity : AppCompatActivity() {

    lateinit var receiver:ForceOfflineReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏标题栏：调用 getSupprotActionBar 获得 ActionBar 的实例，并隐藏。
        supportActionBar?.hide()

        // 知晓当前是哪一个 Activity：
        // javaClass 代表获取当前实例的 Class 对象，相当于 Java 中调用 getClass()
        // BaseActivity::class.java 表示获取此类的 Class 对象，相当于 Java 中调用 BaseActivity.class。
        Log.d("TAG_BaseActivity",javaClass.simpleName)
        ActivityCollector.addActivity(this)
    }

    inner class ForceOfflineReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            AlertDialog.Builder(context).apply {
                setTitle("Warning")
                setMessage("You are forced to offline, Please try to login again.")
                // 对话框不可取消
                setCancelable(false)
                // 注册确定按钮
                setPositiveButton("OK"){_,_ ->
                    // 销毁所有 Activity
                    ActivityCollector.finishAll()
                    val i = Intent(context,MainActivity::class.java)
                    // 重新启动主页面
                    context.startActivity(i)
                }
                show()
            }
        }
    }

    /**
     * 这里使用 onResume() 和 onPause() 来注册和取消注册
     * 因为我们始终需要保证只有处于栈顶的 Activity 才能接收到这条强制下线的广播。
     */
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.broadcast.FORCE_OFFLINE")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }


    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}