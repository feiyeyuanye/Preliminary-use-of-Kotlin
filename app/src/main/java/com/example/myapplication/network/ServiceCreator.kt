package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 构建的 Retrofit 对象是全局通用的，
 * 只需要在调用 create() 时针对不同的 Service 接口传入相应的 Class 类型即可。
 * 因此，可以将通用的部分功能封装起来，从而简化获取 Service 接口动态代理对象的过程。
 * --------------------------------------------------------------------
 * 使用 object 关键字变成一个单例类
 */
object ServiceCreator {

    /**
     * 定义了一个常量
     */
    private const val BASE_URL = "http://10.0.2.2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    /**
     * 定义了一个不带参数的 create()，
     * 并使用 inline 关键字修饰方法，使用 reified 关键字修饰泛型，这是泛型实化的两大前提条件。
     * 接下来就可以使用 T::class.java 这种语法了。
     */
    inline fun <reified T> create():T =
        create(T::class.java)
}