package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.adapter.FruitRVAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.bean.FruitBean
import kotlinx.android.synthetic.main.activity_rv.*
import java.lang.StringBuilder
import java.util.ArrayList

/**
 * 更加强大且推荐的滚动控件
 * 优化了 ListView 存在的不足之处。
 * RecyclerView 属于新增控件，要添加依赖。
 */
class RecyclerViewActivity : BaseActivity(){

    private val fruitList = ArrayList<FruitBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)

        initFruit()
//        val layoutManager = LinearLayoutManager(this)
        // 默认是纵向的，可通过设置来变为横向
        // 为什么 LV 无法实现 RV 的效果：ListView 的布局排列是由自身去管理的，而 RecyclerView 是由 LayoutManager。
        // LayoutManager 制定了一套可扩展的布局排列接口，子类只要按照接口的规范来实现，就能定制出不同排列方式的布局。
        // RV 还有 GridLayoutManager（网格布局），StaggeredGridLayoutManager（瀑布流）效果。
//        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        // 3 列，布局纵向排列
        rv.layoutManager = layoutManager
        val adapter = FruitRVAdapter(fruitList)
        rv.adapter = adapter

    }

    private fun initFruit(){
        // repeat 函数会将表达式内的内容执行 2 遍。
        repeat(3){
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
            fruitList.add(FruitBean(getRandomLengthString("Apple"),R.drawable.ic_launcher_background))
        }
    }

    private fun getRandomLengthString(str:String):String{
        val n = (1..20).random()
        val builder = StringBuilder()
        repeat(n){
            builder.append(str)
        }
        return builder.toString()
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context,RecyclerViewActivity::class.java)
            context.startActivity(intent)
        }
    }
}