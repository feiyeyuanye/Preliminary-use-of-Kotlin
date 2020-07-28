package com.example.myapplication.base

import android.app.Activity

/**
 * 随时随地退出程序
 * 单例类
 */
object ActivityCollector{

    private val activities = ArrayList<Activity>()

    fun addActivity(activity:Activity){
        activities.add(activity)
    }

    fun removeActivity(activity: Activity){
        activities.remove(activity)
    }

    fun finishAll(){
        for (activity in activities){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}