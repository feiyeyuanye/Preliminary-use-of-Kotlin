package com.example.myapplication.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.bean.AppBean
import com.example.myapplication.kt.startActivity
import com.example.myapplication.utils.AppService
import com.example.myapplication.utils.ServiceCreator
import kotlinx.android.synthetic.main.activity_retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        btnGet.setOnClickListener{
//            // 构建 Retrofit 对象
//            val retrofit = Retrofit.Builder()
//                .baseUrl("http://10.0.2.2/")   // 根路径
//                .addConverterFactory(GsonConverterFactory.create())  // 指定 Retrofit 在解析数据时所使用的转换库
//                .build()
//            // 创建一个该接口的动态代理对象，然后就可以随意调用接口中定义的方法，而 Retrofit 会自动执行具体的处理。
//            val appService = retrofit.create(AppService::class.java)

            // 使用封装类来获取一个 AppService 接口的动态代理对象
//            val appService = ServiceCreator.create(AppService::class.java)
            // 通过泛型实化优化上面代码
            val appService = ServiceCreator.create<AppService>()

            // appService.getAppData() 返回一个 Call<List<AppBean>> 对象，再调用它的 enqueue() ，
            // Retrofit 就会根据注解中配置的服务器接口地址去进行网络请求，服务器响应的数据会回调到 enqueue() 中传入的 Callback 实现里。
            // 当发起请求时，Retrofit 会自动在内部开启子线程，当数据回调到 Callback 中之后，Retrofit 又会自动切换回主线程。
            appService.getAppData().enqueue(object : Callback<List<AppBean>> {
                override fun onResponse(call: Call<List<AppBean>>, response: Response<List<AppBean>>) {
                    // 得到 Retrofit 解析后的对象，也就是 List<AppBean> 类型的数据。
                        val list = response.body()
                        if (list != null){
                            for (app in list){
                                Log.d("TAG","id is ${app.id}")
                                Log.d("TAG","name is ${app.name}")
                                Log.d("TAG","version is ${app.version}")
                            }
                       }
                    }

                override fun onFailure(call: Call<List<AppBean>>, t: Throwable) {
                    t.printStackTrace()
                }
           })
        }
    }

    companion object{
        fun actionStart(context: Context){
            startActivity<RetrofitActivity>(context)
        }
    }
}
