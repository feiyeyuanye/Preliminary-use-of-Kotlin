package com.example.myapplication.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_contentprovider.*

/**
 * CP 主要用于在不同应用之间实现数据共享的功能，它提供了一套完整的机制，允许一个程序访问另一个程序中的数据，
 * 同时还能保证被访问数据的安全性。目前，使用 CP 是 Android 实现跨程序共享数据的标准方式。
 *
 * 不同于文件存储和 SharedPreferences 存储中的两种全局可读写操作模式，CP 可以选择只对哪一部分数据进行共享，
 * 从而保证程序中的隐私数据不会有泄露风险。
 *
 * CP 的用法一般有两种：
 * 使用现有 CP 读取和操作相应程序中的数据。（在自己的程序中访问其他应用程序的数据，获得该应用程序的内容 URI，借助 ContentResolver 进行操作）
 * 创建自己的 CP，给程序的数据提供外部访问接口。（新建 MyProvider 类去继承 ContentProvider，并重写全部的 6 个方法）
 */
class ContentProviderActivity:BaseActivity() {

    /**
     * 想要访问 CP 中共享的数据，一定要借助 ContentResolver 类。
     * 通过 Context 中的 getContentResolver() 获取 ContentResolver 类的实例。
     * ContentResolver 类中提供了一系列的方法用于对数据进行增删改查：insert()、update()、delete()、query()。
     *
     * 不同于 SQLite ,CP 中的增删改查方法都是不接收表名参数的，而是使用一个 Uri 参数代替，这个参数被称为内容 URI。
     * 内容 URI 给 ContentProvider 中的数据建立了唯一标识符，它主要由两部分组成：authority 和 path。
     * authority 用于对不同应用程序作区分，一般为了避免冲突，会采用应用包名的方式进行命名。
     * 比如此应用对应的 authority 就可以命名为 com.example.myapplication.provider。
     * path 是用于对同一应用程序中不同的表做区分，通常会添加到 authority 的后面。
     * 比如某个应用的数据库里存在两张表，table1 和 table2，这时就可以将 path 分别命名为 /table1 和 /table2，
     * 然后把 authority 和 path 进行组合，内容 URI 就变成了：
     * com.example.myapplication.provider/table1 和 com.example.myapplication.provider/table2。
     * 为了容易辨识，还需要在字符串的头部加上协议声明，最标准的内容 URI 格式如下：
     * content://com.example.myapplication.provider/table1
     * content://com.example.myapplication.provider/table2
     * 得到了内容 URI 字符串后，还需要将它解析成 Uri 对象才可以作为参数传入，解析的方法如下：
     * val uri = Uri.parse("content://com.example.myapplication.provider/table1")
     *
     * 使用这个 Uri 对象查询 table1 表中的数据：
     * // 参数 1，指定查询某个应用程序下的某一张表
     * // 参数 2，指定查询的列名
     * // 参数 3，指定 where 的约束条件
     * // 参数 4，为 where 中的占位符提供具体的值
     * // 参数 5，指定查询结果的排列方式
     * val cursor = contentResolver.query(uri,projection,selection,selectionArgs,sortOrder)
     * 查询完成后，返回的仍然是一个 Cursor 对象。
     * 查询操作：
     * while(cursor.moveToNext()){
     *  val column1 = cursor.getString(cursor.getColumnIndex("column1"))
     *  val column2 = cursor.getString(cursor.getColumnIndex("column2"))
     * }
     * cursor.close()
     * 向 table1 表中添加一条数据：
     * val values = contentValuesOf("column1" to "text","column2" to 1)
     * contentResolver.insert(uri, values)
     * 更新操作：
     * val values = contentValuesOf("column1" to "")
     * // 使用 selection 和 selectionArgs 参数对想要更新的数据进行约束，防止所有的行都受影响。
     * contentResolver.update(uri, values,"column1 = ? and column2 = ?",arrayOf("text","1"))
     * 删除操作：
     * contentResolver.delete(uri, "column2 = ?",arrayOf("1"))
     */

    private val contactsList = ArrayList<String>()
    private lateinit var adapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contentprovider)

        initData()
    }

    private fun initData() {
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,contactsList)
        lvContacts.adapter = adapter

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),1)
        }else {
            readContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }else{
                    Toast.makeText(this,"You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 查询系统联系人数据
     */
    private fun readContacts() {
        // 这里 ContactsContract.CommonDataKinds.Phone 类做好了封装，提供了一个 CONTENT_URI 常量，
        // 所以不需要再去解析内容 URI 字符串。
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,null,null,null)?.apply {
            while(moveToNext()){
                // 获取联系人姓名
                val displayName = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                // 获取联系人手机号
                val number = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add("$displayName\n$number")
            }
            adapter.notifyDataSetChanged()
            close()
        }
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, ContentProviderActivity::class.java)
            context.startActivity(intent)
        }
    }
}