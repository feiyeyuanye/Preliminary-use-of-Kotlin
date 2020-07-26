package com.example.myapplication.utils

import com.example.myapplication.bean.AppBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*

/**
 * 接口文件，通常建议以具体的功能种类名开头，并以 Service 结尾。
 */
interface AppService {

    /**
     * GET 请求，并传入请求地址的相对路径，根路径会在稍后设置。
     * 返回值必须声明成 Retrofit 内置的 Call 类型，并通过泛型指定服务器响应的数据应该转换成什么对象。
     * -----------------------------------------------------------------------------
     * Retrofit 还提供了强大的 Call Adapters 功能来允许自定义方法返回值的类型，
     * 比如结合 RxJava 使用就可以将返回值声明成 Observable、Flowable 等类型。
     */
    @GET("get_data.json")
    fun getAppData(): Call<List<AppBean>>


    /**
     * {page} 占位符
     * 当调用此方法发起请求时，Retrofit 就会自动将 page 参数的替换到占位符的位置。
     */
//    @GET("{page}/get_data.json")
//    fun getData(@Path("page") page: Int):Call<AppBean>

    /**
     * 当发起网络请求时，Retrofit 会自动按照带参数 GET 请求的格式将这两个参数构建到请求地址当中。
     */
//    @GET("get_data.json")
//    fun getData(@Query("u") user: String, @Query("t") token:String):Call<AppBean>

    /**
     * Call 的泛型指定成了 ResponseBody，这是因为
     * POST，PUT，PATCH，DELETE 这几种请求类型更多是用于操作服务器上的数据，而不是获取数据，
     * 所以通常它们对服务器响应的数据并不关心，ResponseBody 表示 Retrofit 能够接收任意类型的响应数据，并且不会对响应数据进行解析。
     */
//    @DELETE("data/{id}")
//    fun deleteData(@Path("id") id:String):Call<ResponseBody>

    /**
     * 使用 POST 请求，需要将数据放到 HTTP 请求的 body 部分。借助 @Body 注解完成。
     * 请求时，会自动将对象中的数据转换成 JSON 格式的文本，并放到 HTTP 请求的 body 部分，
     * 服务器在收到请求后只需要从 body 中将这部分数据解析出来即可。
     * 这中写法同样适用于 PUT，PATCH，DELETE 类型的请求提交数据。
     */
//    @POST("data/create")
//    fun createData(@Body data:AppBean):Call<ResponseBody>

    /**
     * 静态 header 声明
     * 这些 header 参数其实就是一个个的键值对。
     */
//    @Headers("User-Agent: okhttp","Cache-Control: max-age=0")
//    @GET("get_data.json")
//    fun getData():Call<AppBean>

    /**
     * 发起网络请求时，Retrofit 会自动将参数中传入的值设置到 User-Agent 和 Cache-Control 中。
     */
//    @GET("get_data.json")
//    fun getData1(@Header("User-Agent") userAgent: String, @Header("Cache-Control") cacheControl:String):Call<AppBean>
}