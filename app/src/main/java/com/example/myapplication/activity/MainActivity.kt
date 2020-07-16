package com.example.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {

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
            val intent = Intent(this, Main2Activity::class.java)
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

        btnMainLV.setOnClickListener(this)
        btnMainRV.setOnClickListener(this)
        btnMainChat.setOnClickListener(this)
        btnFragment.setOnClickListener(this)
        btnBroadCast.setOnClickListener(this)
        // 保存在内存中的数据是处于瞬时状态的，而保存在存储设备中的数据是处于持久状态的。
        // 持久化技术提供了一种机制，可以让数据在瞬时状态和持久状态之间进行转换。
        // Android 系统中主要提供了 3 中方式用于简单地实现数据持久化功能：
        // 文件存储、SharedPreferences 存储、数据库存储。
        btnFile.setOnClickListener(this)
        btnSP.setOnClickListener(this)
        btnSQLite.setOnClickListener(this)
        // 动态权限
        btnPermission.setOnClickListener(this)
        // 跨程序共享数据
        btnCP.setOnClickListener(this)
        // 通知
        btnNoti.setOnClickListener(this)

    }

//    private val data = arrayListOf("A","B","C","D","F","G","A","B","C","D","F","G","A","B","C","D","F","G","A","B","C","D","F","G")

    override fun onClick(v: View?) {
        // 不要忘记添加监听事件
        when(v?.id){
            R.id.btnMainLV -> {
//                ListViewActivity.actionStart(this,data)
                ListViewActivity.actionStart(this)
            }
            R.id.btnMainRV -> {
                // RecyclerView
                RecyclerViewActivity.actionStart(this)
            }
            R.id.btnMainChat -> {
                // 聊天界面
                ChatActivity.actionStart(this)
            }
            R.id.btnFragment -> {
                FragmentActivity.actionStart(this)
            }
            R.id.btnBroadCast -> {
                BroadcastActivity.actionStart(this)
            }
            R.id.btnFile ->{
                FileActivity.actionStart(this)
            }
            R.id.btnSP ->{
                SharedPreferencesActivity.actionStart(this)
            }
            R.id.btnSQLite ->{
                SQLiteActivity.actionStart(this)
            }
            R.id.btnPermission ->{
                PermissionActivity.actionStart(this)
            }
            R.id.btnCP ->{
                ContentProviderActivity.actionStart(this)
            }
            R.id.btnNoti ->{
                NotificationActivity.actionStart(this)
            }
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
