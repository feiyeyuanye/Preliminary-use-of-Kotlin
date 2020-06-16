package com.example.myapplication.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment 的生命周期：
 * 添加一个 Fragment ->
 * onAttach(),onCreate(),onCreateView(),onActivityCreated(),onStart(),onResume()
 * Fragment 已激活 ->
 * 1. 用户点击返回键或 Fragment 被移除/替换：
 * onPause(),onStop(),onDestroyView(),onDestroy(),onDetach()
 * 2. 当 Fragment 被添加到返回栈，然后被移除/替换：
 * onPause(),onStop(),onDestroyView() -> 从返回栈中回到上一个 Fragment -> onCreateView()
 * Fragment 被销毁。
 *
 *
 * Fragment 的状态分为：
 * 1. 运行状态：当其所关联的 Activity 正处于运行状态时。
 * 2. 暂停状态：当一个 Activity 进入暂停状态。
 * 3. 停止状态：当一个 Activity 进入停止状态，或者通过调用 FragmentTransaction 的 remove、replace() 将其从 Activity 中移除，
 * 但在事务提交之前 调用 了 addToBackStack()，这时的 Fragment 也会进入停止状态，总的来说，此状态是对用户完全不可见的，可能会被系统回收。
 * 4. 销毁状态：Fragment 总是依附于 Activity 而存在，会随 Activity 的销毁而进入销毁状态。或者通过调用 FragmentTransaction 的 remove、replace() 将其从 Activity 中移除，
 * 但在事务提交之前 并没有调用 了 addToBackStack()，此时会进入销毁状态。
 */
class LifeCycleFragment :Fragment() {



    companion object{
        // 定义了一个 TAG 常量
        const val TAG = "TAG_LifeCycleFragment"
    }

    /**
     * 多了几个与 Activity 不同的生命周期
     * 当与 Activity 建立关联时调用
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 为 Fragment 创建视图（加载布局）时调用
     *
     * onCreate(),onCreateView(),onActivityCreated 三个方法中：
     * 可通过 savedInstanceState 参数获取 onSaveInstanceState() 保存的数据
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 确保与 Fragment 相关联的 Activity 已经创建完毕时调用
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    /**
     * 当与 Fragment 关联的视图被移除时调用
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 当 Fragment 和 Activity 解除关联时调用
     */
    override fun onDetach() {
        super.onDetach()
    }

}