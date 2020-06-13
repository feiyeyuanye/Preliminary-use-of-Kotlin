package com.example.myapplication.test

import java.lang.StringBuilder

fun main(){
    val list = listOf("Apple","Banana","Pear")

    val builder = StringBuilder()
    builder.append("Start eating fruits.\n")
    for (fruit in list){
        builder.append(fruit).append("\n")
    }
    builder.append("Ate all fruits.")
    val result = builder.toString()
    println(result)

    withFun(list)
    runFun(list)
    applyFun(list)
}

/**
 * with 函数接收两个参数：
 * 第一个参数可以是一个任意类型的对象，
 * 第二个参数是一个 Lambda 表达式。
 * with 函数会在表达式中提供第一个参数对象的上下文，并使用表达式中的最后一行代码作为返回值。
 * 示例代码：
 * val result = with(obg){
 *     // 这里是 obj 的上下文
 *     "value" // with 函数的返回值
 * }
 */
fun withFun(list: List<String>){
    // with 函数可以在连续调用同一个对象的多个方法时让代码变得更加精简。
    // 传入 StringBuilder() 对象，接下来表达式的上下文就是这个对象，所以就不用再调用 builder.。
    val result = with(StringBuilder()){
        append("Start eating fruits.\n")
        for (fruit in list){
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        // 表达式的最后一行会作为返回值
        toString()
    }
    println(result)
}

/**
 * run 函数与 with 函数类似
 * 首先 run 函数不能直接调用，而是一定要调用某个对象的 run 函数才行，
 * 其次 run 函数只接收一个 Lambda 参数，并且会在表达式中提供调用对象的上下文
 * 示例代码：
 * val result = obj.run{
 *    // 这里是 obj 的上下文
 *    "value"  // run 函数的返回值
 * }
 */
fun runFun(list: List<String>){
    // 与 with 函数相比，变化非常小
    val result = StringBuilder().run {
        append("Start eating fruits.\n")
        for (fruit in list){
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        // 表达式的最后一行会作为返回值
        toString()
    }
    println(result)
}

/**
 * apply 函数 与 run 函数类似，
 * 都要在某个对象上调用，并且只接收一个 Lambda 参数，也会在表达式中提供调用对象的上下文，
 * 区别在于，apply 函数无法指定返回值，而是会自动返回调用对象本身。
 * 示例代码：
 * val result = obj.apply(){
 *   // 这里是 obj 的上下文
 * }
 * // result == obj
 */
fun applyFun(list: List<String>){
    val result = StringBuilder().apply {
        append("Start eating fruits.\n")
        for (fruit in list){
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        // 表达式的最后一行会作为返回值
        toString()
    }
    // 这里的 result 实际上是一个 StringBuilder 对象
    println(result.toString())
}
