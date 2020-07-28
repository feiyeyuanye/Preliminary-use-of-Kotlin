package com.example.myapplication.storage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_file.*
import java.io.*
import java.lang.StringBuilder


/**
 * 文件存储
 *
 * 它不对存储的内容进行任何格式化处理，所有数据都是原封不动地保存到文件当中的，
 * 因而它比较适合存储一些简单的文本数据或二进制数据。
 * 如果想保存一些较为复杂的结构化数据，就需要定义一套自己的格式规范，方便之后将数据从文件中重新解析出来。
 */

class FileActivity :BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        // 从文件中读取数据
        val inputText = load()
        if (inputText.isNotEmpty()){
            et.setText(inputText)
            // 将光标移动到末尾
            et.setSelection(inputText.length)
            Toast.makeText(this,"Restoring succeeded",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Context 类中提供了一个 openFileInput() 用于从文件中读取数据，它接收一个参数，即要读取的文件名。
     */
    private fun load(): String {
        val content = StringBuilder()
        // FileInputStream -> InputStreamReader -> BufferedReader
        try {
            // 返回一个 FileInputStream 对象
            val input = openFileInput("data")
            // 借助 FileInputStream 构建出一个 InputStreamReader 对象，
            // 接着使用 InputStreamReader 构建出一个 BufferedReader 对象，
            // 然后通过 BufferedReader 将文件中的数据一行行读取出来，拼接到 SB 对象当中。
            val reader = BufferedReader(InputStreamReader(input))
            reader.use{
                // forEachLine 函数也是 Kotlin 提供的一个内置扩展函数
                // 它会将读取到的每行内容都回调到 Lambda 表达式中，然后在表达式中完成拼接即可。
                reader.forEachLine {
                    content.append(it)
                }
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return content.toString()
    }


    /**
     * Context 类中提供了一个 openFileOutput()，可以用于将数据存储到指定的文件中。它接收两个参数：
     * 第一个参数是文件名，在文件创建的时候使用，注意这里指定的文件名不可以包含路径，
     * 因为所有的文件都默认存储到/data/data/<package name>/files/目录下。
     * 第二个参数是文件的操作模式，主要有两种模式可选：（还有两种危险的模式已在 Android 4.2 版本中被废弃）
     * MODE_PRIVATE：默认的，表示当指定相同文件名时，所写入的内容将会覆盖原文件中的内容。
     * MODE_APPEND：表示如果该文件已存在，就往文件里面追加内容，不存在就创建新文件。
     */
    private fun save(inputText:String){
        // FileOutputStream -> OutputStreamWriter -> BufferedWriter -> write() 将内容写入文件
        try {
            // 返回一个 FileOutputStream 对象，得到这个对象后就可以使用 Java 流的方式将数据写入文件中了。
            val output = openFileOutput("data",Context.MODE_PRIVATE)
            // 借助 FileOutputStream 对象构建出一个 OutputStreamWriter 对象，
            // 接着再使用 OutputStreamWriter 对象构建出一个 BufferedWriter 对象，
            // 然后通过 BufferedWriter 对象将文本内容写入文件中。
            val writer = BufferedWriter(OutputStreamWriter(output))
            // use 函数是 Kotlin 提供的一个内置扩展函数，它会保证在 Lambda 表达式中的代码全部执行完之后自动将外层的流关闭，
            // 这样就不需要写一个 finally 语句，手动关闭流了。
            writer.use {
                it.write(inputText)
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    /**
     * 退出时保存数据
     *
     * 可借助工具查看：Device File Explorer
     * 一般在 AS 右下角，也可以通过快捷键（command 或 Ctrl + shift + A）打开搜索功能。
     * 这个工具其实就相当于一个设备文件浏览器，比如此项目：
     * /data/data/com.example.myapplication/files/data（双击此文件，就能看到保存的内容了。）
     */
    override fun onDestroy() {
        super.onDestroy()
        val inputText = et.text.toString()
        save(inputText)
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, FileActivity::class.java)
            context.startActivity(intent)
        }
    }
}