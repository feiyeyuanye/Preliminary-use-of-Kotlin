package com.example.myapplication.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Application 类要记得在 AndroidManifest.xml 中指定使用这个自定义的，而不是默认的 Application 类。
 */
class MyApplication :Application(){

    /**
     * 平时的很多操作都是在 Activity 中进行的，而 Activity 本身就是一个 Context 对象，所以不担心得不到 COntext 的问题，
     * 但有时会脱离 Activity 类，这时可使用 Application 类来管理程序内一些全局的状态信息，比如全局 Context。
     * 以静态变量的形式提供全局获取 Context
     * 使用方式：MyApplication.context
     */
    companion object{
        // 由于 Application 中的 Context 全局只会存在一份实例，并且在整个应用程序的生命周期内都不会回收，因此是不存在内存泄漏风险的，
        // 所以这里可以使用注解，让 Android Studio 忽略可能会产生内存泄漏的警告提示。
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}