package com.example.myapplication

import android.os.Bundle
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.utils.ActivityCollector
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        btn_main3.setOnClickListener{
            // 退出程序
            ActivityCollector.finishAll()
            // 还可以在销毁所以 Activity 的代码后面再加上杀掉当前进程的代码，以保证程序完全退出
            // killProcess() 用于杀掉一个进程，它接收一个进程 id 参数，并且它只能用于杀掉当前进程，不能用于杀掉其它进程。
            // myPid() 获得当前进程的进程 id。
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}
