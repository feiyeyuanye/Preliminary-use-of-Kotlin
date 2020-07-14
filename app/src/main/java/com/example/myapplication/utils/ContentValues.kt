package com.example.myapplication.utils

import android.content.ContentValues

/**
 * 虽然从功能性上面看好像用不到高阶函数的知识，但从代码实现上，却可以结合高阶函数来进行进一步的优化
 * apply 函数的返回值就是它的调用对象本身，因此这里可以使用单行代码函数的语法糖，用等号替代返回值的声明。
 * 另外 ，apply 函数的 Lambda 表达式中会自动拥有 ContentValues 的上下文，所以可直接调用 put 方法。
 */
fun cvOf(vararg pairs: Pair<String, Any?>) = ContentValues().apply{
    for (pair in pairs){
        val key = pair.first
        val value = pair.second
        when(value){
            // 这里还使用了 Kotlin 中的 Smart Cast 功能。
            // 比如 when 语句进入 Int 条件分支后，这个条件下面的 value 会被自动转换成 Int 类型，而不再是 Any? 类型，
            // 这样就不需要像 Java 中那样再额外进行一次向下转型了，这个功能在 if 语句中也同样适用。
            is Int -> put(key, value)
            is Long -> put(key, value)
            is Short -> put(key, value)
            is Float -> put(key, value)
            is Double -> put(key, value)
            is Boolean -> put(key, value)
            is String -> put(key, value)
            is Byte -> put(key, value)
            is ByteArray -> put(key, value)
            null -> putNull(key)
        }
    }
}

/**
 * 这个方法的作用是构建一个 ContentValues 对象。
 *
 * mapOf() 函数允许使用 "Apple" to 1 这样的语法结构快速创建一个键值对。
 * 在 Kotlin 中使用 A to B 这样的语法结构会创建一个 Pair 对象。
 *
 * 方法接收一个 Pair 参数，vararg 关键字对应的是 Java 中的可变参数列表，
 * 允许向这个方法传入 0 个、1 个甚至任意多个 Pair 类型的参数，
 * 这些参数都会被赋值到使用 vararg 声明的这一个变量上面，然后使用 for-in 循环可以将传入的所有参数遍历出来。
 *
 * Pair 是一种键值对的数据结构，因此需要通过泛型来指定它的键和值分别对应什么类型的数据。
 * ContentValues 的键都是字符串类型，所以可直接将 Pair 键的泛型指定成 String，
 * 但 ContentValues 的值可以有多种类型（字符串型、整型、浮点型、甚至是 null），所以要指定成 Any，
 * Any 是 Kotlin 中所有类的共同基类，相当于 Java 的 Object，而 Any？表示允许传入空值。
 */
//fun cvOf(vararg pairs: Pair<String, Any?>): ContentValues{
//    // 创建 ContentValues 对象
//    val cv = ContentValues()
//    // 遍历 pairs 参数列表,取出其中的数据并填入 ContentValues 中,最终将 ContentValues 对象返回.
//    for (pair in pairs){
//        val key = pair.first
//        val value = pair.second
//        // 使用 when 语句一一进行条件判断，并覆盖 ContentValues 所支持的所有数据类型。
//        // （因为 Pair 参数的值是 Any？类型）
//        when(value){
//            // 这里还使用了 Kotlin 中的 Smart Cast 功能。
//            // 比如 when 语句进入 Int 条件分支后，这个条件下面的 value 会被自动转换成 Int 类型，而不再是 Any? 类型，
//            // 这样就不需要像 Java 中那样再额外进行一次向下转型了，这个功能在 if 语句中也同样适用。
//            is Int -> cv.put(key, value)
//            is Long -> cv.put(key, value)
//            is Short -> cv.put(key, value)
//            is Float -> cv.put(key, value)
//            is Double -> cv.put(key, value)
//            is Boolean -> cv.put(key, value)
//            is String -> cv.put(key, value)
//            is Byte -> cv.put(key, value)
//            is ByteArray -> cv.put(key, value)
//            null -> cv.putNull(key)
//        }
//    }
//    return cv
//}