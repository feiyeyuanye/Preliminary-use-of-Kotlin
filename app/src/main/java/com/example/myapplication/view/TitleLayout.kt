package com.example.myapplication.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.example.myapplication.R
import kotlinx.android.synthetic.main.title.view.*

/**
 * 统一的标题栏
 */
class TitleLayout(context: Context,attrs: AttributeSet) : LinearLayout(context,attrs){

    init {
        // 构建 LayoutInflater 对象，调用 inflate() 动态加载布局
        // 参数 this 是指给加载好的布局再添加一个父布局，这里想要指定为 TitleLayout，于是直接传入 this。
        LayoutInflater.from(context).inflate(R.layout.title,this)

        btnTitleBack.setOnClickListener{
            // TitleLayout 接收的 context 参数实际上是一个 Activity 的实例，要先将它转换成 Activity 类型。
            val activity = context as Activity
            activity.finish()
        }

        btnTitleEdit.setOnClickListener{
            Toast.makeText(context,"Toast",Toast.LENGTH_SHORT).show()
        }
    }
}