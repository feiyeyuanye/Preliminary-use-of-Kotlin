package com.example.myapplication.kt

import java.lang.Exception

/**
 * 密封类示例：
 * 用于表示某个操作的执行结果
 */
//interface Result
//
//class Success(val msg:String) : Result
//class Failure(val error:Exception) :Result
//
//fun  getResultMsg(result: Result) = when(result) {
//    is Success -> result.msg
//    is Failure -> result.error
//    // 这个 else 分支实际上是必须的，但又是没有意义的，它只是为了满足编译器的语法检查
//    // 因为结果只有成功了失败，走不到这里.
//    // 但还是有个潜在风险，比如新增了一个 Unknown 类并实现了 Result 接口，用于表示未知的执行结果，
//    // 但是却忘记在上面添加条件分支，这就会走到 else 分支里。
//    else -> IllegalArgumentException()
//}


/**
 * sealed class 表示密封类
 * 密封类及其所有子类只能定义在同一个文件的顶层位置，不能嵌套在其他类中，这是被密封类底层的实现机制所限制的。
 */
sealed class Result

/**
 * 密封类是一个可继承的类，因此在继承它的时候需要在后面添加括号
 */
class Success(val msg:String) : Result()
class Failure(val error:Exception) : Result()

/**
 * 为什么没有 else 分支也能编译通过？
 * 因为当 when 语句传入一个密封类变量作为条件时，
 * Kotlin 编译器会自动检查该密封类有哪些子类，并强制要求将每一个子类所对应的条件全部处理。
 */
fun  getResultMsg(result: Result) = when(result) {
    is Success -> result.msg
    is Failure -> result.error
    // 改成密封类之后，就不需要 else 分支了。
}