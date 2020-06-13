package com.example.myapplication.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.utils.ActivityCollector

open class BaseActivity : AppCompatActivity() {
    /**
     * 知晓当前是哪一个 Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏标题栏：调用 getSupprotActionBar 获得 ActionBar 的实例，并隐藏。
        supportActionBar?.hide()

        // javaClass 代表获取当前实例的 Class 对象，相当于 Java 中调用 getClass()
        // BaseActivity::class.java 表示获取此类的 Class 对象，相当于 Java 中调用 BaseActivity.class。
        Log.d("TAG_BaseActivity",javaClass.simpleName)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}