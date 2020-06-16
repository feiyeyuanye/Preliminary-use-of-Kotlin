package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.MsgAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.bean.ChatMsgBean
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity:BaseActivity(), View.OnClickListener {

    private val msgList = ArrayList<ChatMsgBean>()
    /**
     * 可以使用 lateinit 关键字，进行延迟初始化，
     * 这样就不用在一开始时将它赋值为 null 了，类型声明可以改成 MsgAdapter。
     * 但使用要注意，要确保变量在使用前，已做了初始化。
     */
//    private var adapter:MsgAdapter?=null
    private lateinit var adapter:MsgAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initMsg()
        val layoutManager = LinearLayoutManager(this)
        rvChat.layoutManager = layoutManager

        // 使用 ::adapter.isInitialized 可用于判断 adapter 变量是否已经初始化，避免重复进行初始化操作。
        if (!:: adapter.isInitialized){
            adapter = MsgAdapter(msgList)
        }
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
                    // 有时，像即使明确知道一些全局变量不会为空，onClick() 会在 onCreate() 之后调用，在 onCreate()中对 adapter 做了初始化。
                    // 但出于 Kotlin 编译器的要求，还是需要额外做许多的判空处理代码。但做了延迟初始化之后，就不必了。
                    // adapter?.notifyItemInserted(msgList.size-1)
                    adapter.notifyItemInserted(msgList.size-1)
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
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }
    }
}