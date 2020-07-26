package com.example.myapplication.kt

import android.content.Context
import android.content.Intent


/**
 * Intent 的第二个参数本该是一个具体的 Activity 的 Class 类型，
 * 但由于 T 已经是一个被实化的泛型了，因此这里可直接传入 T::class.java。
 */
inline fun <reified T> startActivity(context: Context){
    val intent = Intent(context,T::class.java)
    context.startActivity(intent)
}


/**
 * 增加了一个函数类型参数，并且它的函数类型是定义在 Intent 类当中的
 */
inline fun <reified T> startActivity(context: Context,block:Intent.() ->Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context.startActivity(intent)
}