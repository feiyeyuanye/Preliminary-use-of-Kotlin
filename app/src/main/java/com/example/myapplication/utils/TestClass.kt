package com.example.myapplication.utils

class TestClass {

    fun doAction1(){

    }

    /**
     * @JvmStatic 注解，会使 Kotlin 编译器将这些方法编译成真正的静态方法。
     * 这个注解只能加在单例类或者 companion object 中的方法上
     */
    companion object {
        @JvmStatic
        fun doAction2(){

        }
    }
}