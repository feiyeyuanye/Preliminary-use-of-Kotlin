package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.myapplication.R

/**
 * 继承 Fragment，两个不同包下的供选择：
 * AndroidX 库中的：androidx.fragment.app.Fragment（可使特性在不同系统版本保持一致）
 * 系统内置的：android.app.Fragment（9.0 版本中被废弃）
 */
class RightFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 通过 LayoutInflater 的 inflate() 动态加载布局
        return inflater.inflate(R.layout.fragment_right,container,false)
    }
}