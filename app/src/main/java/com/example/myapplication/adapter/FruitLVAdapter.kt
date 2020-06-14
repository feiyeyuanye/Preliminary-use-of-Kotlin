package com.example.myapplication.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.bean.FruitBean

class FruitLVAdapter(activity:Activity, val resourceId:Int, data:ArrayList<FruitBean>):
ArrayAdapter<FruitBean>(activity,resourceId,data){

    /**
     * 使用 ViewHolder 对控件实例进行缓存
     * 使用 inner class 关键字来定义内部类
     */
    inner class ViewHolder(val fruitImage:ImageView,val fruitName:TextView)

    /**
     * 提升 ListView 的运行效率
     * 优化一：默认在 getView() 中每次都将布局重新加载一遍，当快速滚动时，会有性能瓶颈。
     *       使用 convertView 参数优化，它用于将之前加载好的布局进行缓存。
     * 优化二：每次仍会调用 findViewById() 获取一次控件的实例。
     *       可借助一个 ViewHolder 来对这部分优化。
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view:View
        val viewHolder : ViewHolder
        if (convertView == null){
            // false 表示只让我们在父布局中声明的 layout 生效，但不会为这个 View 添加父布局。
            // 因为一旦 View 有了父布局后，它就不能再添加到 ListView 中了。
            view = LayoutInflater.from(context).inflate(resourceId,parent,false)

            // 'kotlin-android-extensions' 插件在适配器中是无法工作的。
            val ivItemFruit : ImageView = view.findViewById(R.id.ivItemFruit)
            val tvItemFruit : TextView = view.findViewById(R.id.tvItemFruit)
            viewHolder = ViewHolder(ivItemFruit ,tvItemFruit)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }


        // 获取当前项的 FruitBean 实例
        val fruit = getItem(position)
        if (fruit != null)
        {
            viewHolder.fruitImage.setImageResource(fruit.imgId)
            viewHolder.fruitName.text = fruit.name
        }
        return view
    }


}