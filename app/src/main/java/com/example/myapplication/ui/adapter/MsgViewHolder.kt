package com.example.myapplication.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

/**
 * 密封类，结合 RecyclerView 适配器的 ViewHolder
 */
sealed class MsgViewHolder(view:View):RecyclerView.ViewHolder(view){

    class LeftViewHolder(view: View): MsgViewHolder(view){
        val leftMsg : TextView = view.findViewById(R.id.tvMsgLeft)
    }

    class RightViewHolder(view: View): MsgViewHolder(view){
        val rightMsg : TextView = view.findViewById(R.id.tvMsgRight)
    }

}