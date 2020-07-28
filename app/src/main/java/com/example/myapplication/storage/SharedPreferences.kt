package com.example.myapplication.storage

import android.content.SharedPreferences


/**
 * 通过扩展函数的方式向 SP 中添加了一个 open 函数，并且它还接收一个函数类型的参数，因此 open 函数自然就是一个高阶函数。
 *
 * 由于 open 函数内拥有 SP 的上下文，因此这里可直接调用 edit() 来获取 SharedPreferences.Edit() 对象。
 *
 * 另外 open 函数接收的是一个 SharedPreferences.Editor 的函数类型参数，因此这里需要调用 editor.block() 对函数类型参数进行调用，
 * 就可以在函数类型参数的具体实现中添加数据了。
 *
 * 最后还要调用 editor.apply() 来提交数据，从而完成数据存储操作。
 */
fun SharedPreferences.open(block: SharedPreferences.Editor.() -> Unit){
    val editor = edit()
    editor.block()
    editor.apply()
}