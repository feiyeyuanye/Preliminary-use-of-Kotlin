package com.example.myapplication.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.adapter.FruitLVAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.bean.FruitBean
import kotlinx.android.synthetic.main.activity_lv.*
import java.util.ArrayList

/**
 * ListView 的性能需要一些技巧来提升。
 * ListView 的扩展性不够好，并且只能实现数据的纵向滚动效果。
 */
class ListViewActivity : BaseActivity(){

    private val fruitList = ArrayList<FruitBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lv)

//        val  data = intent.getStringArrayListExtra("param1")
//        // 最好用的适配器 ArrayAdapter，它通过泛型来指定要适配的数据类型。
//        // R.layout.simple_list_item_1 系统内置的布局文件，内部只有一个 TextView。
//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data)

        initFruit()
        val adapter = FruitLVAdapter(this, R.layout.item_fruit,fruitList)
        lv.adapter = adapter
//        lv.setOnItemClickListener{parent, view, position, id ->
//            val fruit = fruitList[position]
//            Toast.makeText(this,fruit.name,Toast.LENGTH_SHORT).show()
//        }
        // Kotlin 更加推荐将没有用到的参数使用下划线来替代，但参数的位置不能变。
        lv.setOnItemClickListener{ _, _, position, _ ->
            val fruit = fruitList[position]
            Toast.makeText(this,fruit.name,Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFruit(){
        // repeat 函数会将表达式内的内容执行 2 遍。
        repeat(3){
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
            fruitList.add(FruitBean("Apple",
                R.drawable.ic_launcher_background
            ))
        }
    }


    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, ListViewActivity::class.java)
            context.startActivity(intent)
        }
    }
}