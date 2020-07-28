package com.example.myapplication.contentprovider

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_permission.*

/**
 * 运行时权限的核心，就是在程序运行过程中，由用户授权我们去执行某些危险操作。
 */
class PermissionActivity :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        // 申请运行时权限
        btnCall.setOnClickListener{
            // 先判断用户是否已经给过授权了，借助的是 ContextCompat.checkSelfPermission()，接收两个参数：
            // 第一个参数是 Context。
            // 第二个参数是具体的权限名，比如打电话的权限名是 Manifest.permission.CALL_PHONE。
            // 然后使用方法的返回值和 PackageManager.PERMISSION_GRANTED 比较，相等就说明用户已经授权。
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                // 如果不相等，说明未授权，则需要调用 ActivityCompat.requestPermissions() 向用户申请授权，它接收三个参数：
                // 第一个参数要求是 Activity 的实例
                // 第二个参数是一个 String 数组，把要申请的权限名放在数组中即可。
                // 第三个参数是请求码，只要是唯一值就可以。
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),1)
            }else {
                call()
            }
        }
    }

    /**
     * 调用完 requestPermissions() 后，系统会弹出一个申请权限的对话框，不论是否同意，最终都会回调到这个方法中。
     * 授权的结果会封装在 grantResults 参数中。
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            // 匹配请求码
            1 -> {
                // 判断授权结果
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call()
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun call(){
            // 打开拨号界面,不需要声明权限.
//            val intent = Intent(Intent.ACTION_DIAL)

        // 直接拨打电话,危险权限.清单文件声明和动态申请权限,都需要.
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:1008611")
            startActivity(intent)
        }catch (e: SecurityException){
            e.printStackTrace()
        }

    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, PermissionActivity::class.java)
            context.startActivity(intent)
        }
    }
}