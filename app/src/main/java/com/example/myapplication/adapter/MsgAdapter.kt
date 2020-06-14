package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bean.ChatMsgBean
import kotlinx.android.synthetic.main.activity_chat.view.*
import java.lang.IllegalArgumentException

class MsgAdapter (val msgList:List<ChatMsgBean>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LeftViewHolder(view: View):RecyclerView.ViewHolder(view){
        val leftMsg :TextView = view.findViewById(R.id.tvMsgLeft)
    }

    inner class RightViewHolder(view: View):RecyclerView.ViewHolder(view){
        val rightMsg :TextView = view.findViewById(R.id.tvMsgRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == ChatMsgBean.TYPE_RECEIVED){
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_msg_left,parent,false)
        LeftViewHolder(view)
    }else{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_msg_right,parent,false)
        RightViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }

    override fun getItemCount() = msgList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when(holder){
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
            else -> throw IllegalArgumentException()
        }
    }


}