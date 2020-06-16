package com.example.myapplication.test

fun main(){
    val money1 = Money(5)
    val money2 = Money(10)
    // 对象和对象相加
    val money3 = money1 + money2
    println(money3.value)
    // 对象直接和数字相加
    val money4 = money3 + 20
    println(money4.value)

    // String 类中提供的包含关系
    println("hello".contains("he"))
    // 借助重载的语法糖表达式。效果相同，但更简洁。
    println("he" in "hello")
}

class Money(val value:Int){

    /**
     * 运算符重载使用 operator 关键字在指定函数的前面加上就可以了。
     * 指定函数指的是不同运算符对应的重载函数也不同，
     * 比如加号运算符对应的是 plus()，减号运算符对应 minus()，它们都是固定不变的。
     * 但接收的参数和函数返回值可以自行设定，这里就代表一个 Obj 对象可以与另一个 Obj 对象相加，最终返回一个新的 Obj 对象。
     * operator fun plus(obj:Obj){
     *    // 处理相关逻辑
     * }
     *
     * 对应的调用方式如下：
     * val obj1 = Obj()
     * val obj2 = Obj()
     * val obj3 = obj1 + obj2  // 它会在编译时被转换成 obj1.plus(obj2) 的调用方式
     */
    operator fun plus(money:Money):Money{
        // 将当前 Money 对象的 value 和参数传入的 Money 对象的 value 相加，
        // 然后将得到的和传给一个新的 Money 对象并将该对象返回。
        val sum = value + money.value * 6
        return Money(sum)
    }

    /**
     * Kotlin 允许对同一个运算符进行多重重载
     */
    operator fun plus(newValue:Int):Money{
        val sum = value + newValue
        return Money(sum)
    }
}