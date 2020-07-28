package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.bean.FruitBean

class FruitRVAdapter(val fruitList: ArrayList<FruitBean>) :
     RecyclerView.Adapter<FruitRVAdapter.ViewHolder>(){

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivItemFruit : ImageView = view.findViewById(R.id.ivItemFruit)
        val tvItemFruit : TextView = view.findViewById(R.id.tvItemFruit)
    }

    /**
     * 创建 ViewHolder 实例
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.item_fruit,parent,false)
        val viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener{
            // 获取用户点击的 position。因为此方法没有提供。
            val position = viewHolder.adapterPosition
            val fruit = fruitList[position]
            Toast.makeText(parent.context,"you click view ${fruit.name}",Toast.LENGTH_SHORT).show()
        }

        viewHolder.ivItemFruit.setOnClickListener{
            val position = viewHolder.adapterPosition
            val fruit = fruitList[position]
            Toast.makeText(parent.context,"you click img ${fruit.name}",Toast.LENGTH_SHORT).show()

        }

        return viewHolder
    }

    /**
     * 对子项的数据赋值，会在每个子项被滚动到屏幕内的时候执行。
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.ivItemFruit.setImageResource(fruit.imgId)
        holder.tvItemFruit.text = fruit.name
    }

    /**
     * 一共有多少子项
     */
    override fun getItemCount() = fruitList.size

}