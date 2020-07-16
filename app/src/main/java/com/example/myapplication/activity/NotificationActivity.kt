package com.example.myapplication.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity

class NotificationActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initView()
        initData()
    }

    private fun initData() {
        // 获取 NotificationManager 的实例
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

       


    }

    private fun initView() {

    }


    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, ContentProviderActivity::class.java)
            context.startActivity(intent)
        }
    }
}