package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.fragment.AnotherRightFragment
import com.example.myapplication.fragment.LeftFragment
import com.example.myapplication.fragment.RightFragment
import kotlinx.android.synthetic.main.left_fragment.*

/**
 * Android 3.0 版本开始引入了 Fragment 的概念，它可以让界面在平板上更好地展示。
 * Fragment 是一种可以嵌入在 Activity 中的 UI 片段，它能让程序更加合理和充分地利用大屏幕的空间，因而在平板上应用广泛。
 */
class FragmentActivity :BaseActivity(){

    /**
     * Fragment 和 Activity 之间的交互：
     *
     * 通过 FragmentManager，从布局中获取 Fragment 的实例：
     * val fragment = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
     * 并且插件也对 findFragmentById() 进行了扩展，允许直接使用布局文件中定义的 Fragment id 名称来自动获取相应实例。
     * val fragment = leftFrag as LeftFragment
     *
     * 在 Fragment 中可通过 getActivity() 得到和当前 Fragment 相关联的 Activity 实例：
     * if(activity != null){    // getActivity() 可能返回 null
     *  val activity = activity as FragmentActivity()
     * }
     * 另外当 Fragment 中需要 Context 对象时，也可使用 getActivity()，因为获取到的 Activity 本身就是一个 Context 对象。
     *
     * 首先在一个 Fragment 中可得到与之相关联的 Activity，然后通过这个 Activity 获取另一个 Fragment 实例，
     * 这样就实现了不同 Fragment 之间的通信功能。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 限定符（qualifier）：比如 large 限定符，当屏幕被认为是 large 的设备就会加载 layout-large 文件夹下的布局。
        // 常见的比如：
        // 大小：small，normal，large，xlarge。对应（小，中等，大，超大）屏幕设备的资源。
        // 分辨率：ldpi，mdpi，hdpi，xhdpi，xxhdpi。对应（低(120dpi以下)，中等(120-160)，高(160-240)，超高(240-320)，超超高(320-480)）分辨率设备的资源。
        // 方向：land，port。对应（横屏，竖屏）设备的资源。
        // 最小宽度限定符：当程序运行在屏幕宽度大于等于 600 dp 的设备上时，会加载 layout-sw600dp/activity_fragment 布局，否则仍然加载默认的 layout/activity_fragment。
        setContentView(R.layout.activity_fragment)
        btn.setOnClickListener{
            replaceFragment(RightFragment())
        }
        replaceFragment(AnotherRightFragment())
    }

    /**
     * 动态添加 Fragment 主要分为 5 步：
     * 1. 创建待添加 Fragment 实例
     * 2. 获取 FragmentManager，在 Activity 中可直接调用 getSupportFragmentManager() 获取
     * 3. 开启一个事务，通过调用 beginTransaction() 开启
     * 4. 向容器内添加或替换 Fragment，一般使用 replace() 实现，需要传入容器的 id 和待添加的 Fragment 实例
     * 5. 提交事务，调用 commit() 完成。
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.rightLayout,fragment)
        // 在 Fragment 实现返回栈的效果，点击 Back 键回到上一个 Fragment。
        // 不会执行 onCreate()，因为借助了 addToBackStack() 使得 Fragment 没有被销毁。
        // 它接收一个名字用于描述返回栈的状态，一般传入 null 即可。
//        transaction.addToBackStack(null)
        transaction.commit()
    }


    companion object{
        fun actionStart(context: Context){

            val intent = Intent(context, FragmentActivity::class.java)
            context.startActivity(intent)
        }
    }
}