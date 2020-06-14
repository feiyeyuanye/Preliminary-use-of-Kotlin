package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.MsgAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.bean.ChatMsgBean
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity:BaseActivity(), View.OnClickListener {

    private val msgList = ArrayList<ChatMsgBean>()
    private var adapter:MsgAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initMsg()
        val layoutManager = LinearLayoutManager(this)
        rvChat.layoutManager = layoutManager
        adapter = MsgAdapter(msgList)
        rvChat.adapter = adapter
        btnChat.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btnChat ->{
                val content = etChat.text.toString()
                if (content.isNotEmpty()){
                    val msg = ChatMsgBean(content,ChatMsgBean.TYPE_SENT)
                    msgList.add(msg)
                    // 当有新增消息时，刷新 RV 中的显示
                    // 也可以使用 notifyDataSetChanged()，这样不管新增、删除还是修改元素都会刷新界面可见的全部元素，但这样效率相对会差一些。
                    adapter?.notifyItemInserted(msgList.size-1)
                    // 将 RV 定位到最后一行
                    rvChat.scrollToPosition(msgList.size-1)
                    // 清空输入框中的内容
                    etChat.setText("")
                }
            }
        }
    }

    private fun initMsg() {
        val msg1 = ChatMsgBean("Hello",ChatMsgBean.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = ChatMsgBean("Hello",ChatMsgBean.TYPE_SENT)
        msgList.add(msg2)
        val msg3 = ChatMsgBean("Hello",ChatMsgBean.TYPE_RECEIVED)
        msgList.add(msg3)
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context,ChatActivity::class.java)
            context.startActivity(intent)
        }
    }
}