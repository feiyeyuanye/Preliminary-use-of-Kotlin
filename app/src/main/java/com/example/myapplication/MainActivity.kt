package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 'kotlin-android-extensions' 插件的作用，不必再 findViewById()。
        // 这个插件会根据布局文件中定义的控件 id 自动生成一个具有相同名称的变量。
//      android:textAllCaps="false" 取消默认的字母全部大写
        btn_main.setOnClickListener {
            // 这里使用了 Kotlin 的语法糖，实际上调用的是 editText.getText()
            // 不必特意记忆，直接输入 getText() 后，Android Studio 会自动给提示转换。
//            val inputext = editText.text.toString()
//            Toast.makeText(this,inputext,Toast.LENGTH_SHORT).show()

            // 界面跳转，显式 Intent
            val data = "Hello"
            val intent = Intent(this,Main2Activity::class.java)
            // 传递参数
            intent.putExtra("extra_data",data)
//            startActivity(intent)
            startActivityForResult(intent,1)

            // 隐式启动
            // 只有<action>和<category>同时匹配才行
            // 但这里的<category>因为设置的为DEFAULT，所以只需匹配<action>即可，<category>会自动添加到Intent中。
//            val intent = Intent("com.example.activitytest.ACTION_START")
//            intent.addCategory("com.example.activitytest.MY_CATEGORY")
//            startActivity(intent)

            // 更多隐式 Intent 的用法
//            val intent = Intent(Intent.ACTION_VIEW)
//            // 通过 Uri.parse() 将网址字符串解析成一个 Uri 对象，再调用 Intent 的 setData() 将这个 Uri 对象传递进去。
//            intent.data = Uri.parse("https://www.baidu.com")
//            startActivity(intent)

//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:1008611")
//            startActivity(intent)
        }
    }

    /**
     * requestCode：启动 Activity 时传入的请求码
     * resultCode：返回数据时传入的处理结果
     * data：携带着返回数据的 Intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 判断数据来源
        when(requestCode){
            // 判断处理结果是否成功
            1 -> if (resultCode == Activity.RESULT_OK){
                val returnedData = data?.getStringExtra("data_return")
                Log.d("TAG_MainActivity","return data is $returnedData")
            }
        }
    }
}
