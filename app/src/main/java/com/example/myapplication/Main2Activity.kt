package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myapplication.test.BaseActivity
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // intent 实际上调用的是父类的 getIntent()
        val extraData = intent.getStringExtra("extra_data")
        Log.d("TAG_Main2Activity","extra data is $extraData")

        // 返回数据给上一个页面
//        btn_main2.setOnClickListener{
//            val intent = Intent()
//            intent.putExtra("data_return","Hello MainActivity")
//            setResult(Activity.RESULT_OK,intent)
//            finish()
//        }
        btn_main2.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_main2 ->{
                val intent = Intent()
                intent.putExtra("data_return","Hello MainActivity")
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }

    /**
     * 启动 Activity 的最佳写法
     * companion object 语法结构：
     * Kotlin 规定，所有定义在此结构中的方法都可以使用类似于 Java 静态方法的形式调用。
     */
    companion object{
        fun actionStart(context: Context,data1:String,data2:String){
//            val intent = Intent(context,Main2Activity::class.java)
//            intent.putExtra("param1",data1)
//            intent.putExtra("param2",data2)
//            context.startActivity(intent)

            val intent = Intent(context,Main2Activity::class.java).apply {
                putExtra("param1",data1)
                putExtra("param2",data2)
            }
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("data_return","Hello MainActivity")
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}
