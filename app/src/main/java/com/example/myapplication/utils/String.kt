package com.example.myapplication.utils

import java.lang.StringBuilder

/**
 * 扩展函数，重载乘号运算符
 * 参考表：乘法 times()
 */
//operator fun String.times(n:Int):String{
//    val builder = StringBuilder()
//    repeat(n){
//        builder.append(this)
//    }
//    return builder.toString()
//
//}

/**
 * 字符串有了和数字相乘的能力
 * val str = "abc" * 3
 * 打印结果：abcabcabc
 */
operator fun String.times(n:Int) = repeat(n)