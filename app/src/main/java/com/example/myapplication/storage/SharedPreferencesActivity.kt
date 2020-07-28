package com.example.myapplication.storage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sp.*

/**
 * 使用键值对的方式来存储数据，使用 XML 格式对数据进行管理，并且支持多种不同的数据类型存储。
 * -------------------------------------------------------------------------------
 * 要想使用 SharedPreferences 存储，首先需要获取 SharedPreferences 对象。
 * Android 中提供了两种方法用于得到 SharedPreferences 对象：
// 1. Context 类中的 getSharedPreferences() 方法，接收两个参数：
// 第一个参数用于指定 SP 文件的名称，如果指定的文件不存在则会创将一个，
// SP 文件默认路径 /data/data/<package name>/shared_prefs/目录下。
// 第二个参数用于指定操作模式，目前只有默认的 MODE_PRIVATE 这一种模式可选，
// 它和直接传入 0 的效果相同，表示只有当前的应用程序才可以对这个 SP 文件进行读写。
// 其他模式均已被废弃：
// MODE_WORLD_READABLE，MODE_WORLD_WRITEABLE 这两种模式在 Android 4.2 版本中被废弃,
// MODE_MULTI_PROCESS 模式在 Android 6.0 版本中行被废弃。
// 2. Activity 类中的 getPreferences() 方法，接收一个操作模式参数：
// 使用这个方法时会自动将当前 Activity 的类名作为 SP 的文件名
 * -------------------------------------------------------------------------------
 * 得到 SP 对象之后，就可以向其中存储数据了，主要分为 3 步实现：
 * 1. 调用 SP 对象的 edit() 获取一个 SharedPreferences.Editor 对象。
 * 2. 向 SharedPreferences.Editor 对象中添加数据，比如添加一个布尔类型数据就使用 putBoolean()。
 * 3. 调用 apply() 将添加的数据提交，从而完成数据存储操作。
 */
class SharedPreferencesActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp)

        initData()
        rememberPassword()
    }

    /**
     * 实际项目中，不可以以明文的形式直接存储，必须结合一定的加密算法对密码进行保护。
     */
    private fun rememberPassword() {
        val prefs = getPreferences(Context.MODE_PRIVATE)
        // 记住密码按钮是否选中
        val isRemember = prefs.getBoolean("remember_password",false)
        if (isRemember){
            // 将账号和密码都设置到文本框中
            val account = prefs.getString("account","")
            val password = prefs.getString("password","")
            etAccount.setText(account)
            etPassword.setText(password)
            // 将 CheckBox 设为选中
            rememberPass.isChecked = true
        }

        // 登录按钮，并记住密码
        btnLogin.setOnClickListener{
            val account = etAccount.text.toString()
            val password = etPassword.text.toString()
            // 如果账号是 admin 且密码是 123456，就认为登录成功
            if (account == "admin" && password == "123456") {
                val edit = prefs.edit()
                // 检查复选框是否被选中
                if (rememberPass.isChecked){
                    edit.putBoolean("remember_password",true)
                    edit.putString("account",account)
                    edit.putString("password",password)
                }else {
                    // 如果未被选中，意味着不需要记住密码
                    edit.clear()
                }
                edit.apply()
                finish()
            }else{
                Toast.makeText(this,"account or paddword is invalid",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initData() {
        // 读取数据
        btnRestore.setOnClickListener{
            val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
            val name = prefs.getString("name","")
            val age = prefs.getInt("age",0)
            val married = prefs.getBoolean("married",false)
            Log.d("TAG","name is $name")
            Log.d("TAG","age is $age")
            Log.d("TAG","married is $married")
        }

        // 存储数据
        btnSave.setOnClickListener{
//            val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
//            editor.putString("name","Tom")
//            editor.putInt("age",28)
//            editor.putBoolean("married",false)
//            editor.apply()

            // 使用高阶函数的方式来简化 SP 的用法。
//            getSharedPreferences("data",Context.MODE_PRIVATE).open {
//                // 拥有 SharedPreferences.Editor 的上下文环境，因此这里可直接调用相应的 put 方法来添加数据。
//                putString("name","Tom")
//                putInt("age",28)
//                putBoolean("married",false)
//            }

            // 实际上，Google 提供的 KTX 扩展库中已经包含了上述的简化方法，这个扩展库为：
            // implementation 'androidx.core:core-ktx:1.3.0'
            // 但通过上述方法来了解原理，更有助于以后对更多的 API 进行扩展。
            getSharedPreferences("data",Context.MODE_PRIVATE).edit {
                putString("name","Tom")
                putInt("age",28)
                putBoolean("married",false)
            }
        }
    }


    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, SharedPreferencesActivity::class.java)
            context.startActivity(intent)
        }
    }
}