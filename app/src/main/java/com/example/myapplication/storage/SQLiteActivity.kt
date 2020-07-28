package com.example.myapplication.storage

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.contentValuesOf
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sqlite.*
import java.lang.Exception
import java.lang.NullPointerException


/**
 * SQLite 是一款轻量级的关系型数据库，适合存储大量复杂的关系型数据。
 * 它的运算速度非常快，占用资源很少，通常只需要几百 KB 的内存就足够了，因而特别适合在移动设备上使用。
 * -------------------------------------------------------------------------------
 * SQLite 不仅支持标准的 SQL 语法，还遵循了数据库的 ACID 事务。
 */
class SQLiteActivity:BaseActivity() {

    private val TAG = "TAG_SQLiteActivity"
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)

        initData()
    }

    /**
    // Android 专门提供了一个 SQLiteOpenHelper 帮助类用来更加方便地管理数据库。
    // SQLiteOpenHelper 是一个抽象类，需要创建一个自己的帮助类去继承它，
    ----------------------------------------------------------------------------------------------------------------------
    // 有两个抽象方法，并且必须重写。onCreate() 和 onUpgrade()，用来实现创建和升级数据库的逻辑。
    ----------------------------------------------------------------------------------------------------------------------
    // 还有两个非常重要的实例方法，getReadableDatabase() 和 getWritableDatabase()，
    // 它们都可以创建或打开一个现有的数据库（如果数据库已存在则直接打开，否则要创建一个新的数据库），并返回一个可对数据库进行读写操作的对象。
    // 不同的是，当数据库不可写入时（如磁盘空间已满），getReadableDatabase() 返回的对象将以只读的方式打开数据库，而 getWritableDatabase() 则将出现异常。
    ----------------------------------------------------------------------------------------------------------------------
    // SQLiteOpenHelper 中有两个构造方法可供重写，一般使用参数少的那一个即可，它接收 4 个参数：
    // 1. Context，必须有它才能对数据库进行操作。
    // 2. 数据库名，创建数据库时使用的就是这里指定的名称。
    // 3. 允许在查询数据库时返回一个自定义的 Cursor，一般传 null 即可。
    // 4. 当前数据库的版本号，可用于对数据库进行升级操作。
    ----------------------------------------------------------------------------------------------------------------------
    // 构建出 SQLiteOpenHelper 的实例之后，再调用它的 getReadableDatabase() 或 getWritableDatabase() 就能够创建数据库了，
    // 数据库文件存在在 /data/data/<package name>/database/目录下。（安装插件：AS->Preferences->Plugins->搜索 Database Navigator）
    // 此时，重写的 onCreate() 也会得到执行，所以通常会在这里处理一些创建表的逻辑。
     */
    private fun initData() {
        // 构建 MyDatabaseHelper 对象，将版本号升级，就会执行 onUpgrade()
        dbHelper = MyDatabaseHelper(this, "BookStore.db", 3)
        createTable()
        dataCRUD()
        transaction()
    }

    /**
     * SQLite 数据库是支持事务的，
     * 事务的特性可以保证让一系列的操作要么全部完成，要么一个都不会完成。
     * 避免中途出现异常，导致数据丢失。
     */
    private fun transaction() {
        btnReplace.setOnClickListener{
            val db = dbHelper.writableDatabase
            db.beginTransaction()  // 开启事务
            try {
                db.delete("Book",null,null)
                if (true){
                    // 手动抛出一个异常，用来测试，
                    // 由于事务的存在，中途出现异常会导致事务的失败，那么旧数据应该是删除不掉的。
                    throw NullPointerException()
                }

//                val values = ContentValues()
//                values.put("name","Game of Thrones")
//                values.put("author","George Martin")
//                values.put("pages",720)
//                values.put("price",20.85)

                // 使用 apply 函数简化写法
//                val values = ContentValues().apply {
//                    put("name","Game of Thrones")
//                    put("author","George Martin")
//                    put("pages",720)
//                    put("price",20.85)
//                }

                // 使用高阶函数来简化用法
//                val values = cvOf("name" to "Game of Thrones","author" to "George Martin",
//                    "pages" to 720,"price" to 20.85)

                // 实际上，KTX 库中也提供了一个同样功能的方法
                val values = contentValuesOf("name" to "Game of Thrones","author" to "George Martin",
                    "pages" to 720,"price" to 20.85)

                db.insert("Book",null,values)
                db.setTransactionSuccessful() // 事务已经执行成功
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                db.endTransaction()  // 结束事务
            }
        }
    }

    /**
     * 数据操作无非 4 种：C（create 添加）R（retrieve 查询）U（update 更新）D（delete 删除）
     * 每一种操作都对应了一种 SQL 命令：insert，select，update，delete。
     * ------------------------------------------------------------------------------------------------
     * 并且 Android 还提供了一系列辅助性方法，即使不编写 SQL 语句，也能完成所有的 CRUD 操作。
     * getReadableDatabase() 或 getWritableDatabase() 会返回一个 SQLiteDatabase 对象，用来对数据进行 CRUD 操作。
     *
     */
    private fun dataCRUD() {
        insertData()
        updateData()
        deleteData()
        queryData()
    }

    private fun queryData() {
        btnQuery.setOnClickListener{
            val db = dbHelper.writableDatabase
            // 查询 Book 表中所有数据
            // 最短的重载方法，也有 7 个参数：
            // 第一个参数，表名
            // 第二个参数，指定查询哪几列，不指定则默认查询所有列。
            // 第三个参数，用于约束查询某一行或某几行的数据，不指定则默认查询所有行的数据。
            // 第四个参数，用于约束查询某一行或某几行的数据，不指定则默认查询所有行的数据。
            // 第五个参数，指定需要去 group by 的列，不指定则表示不对查询结果进行 group by 操作。
            // 第六个参数，用于对 group by 之后的数据进行进一步的过滤，不指定则表示不进行过滤。
            // 第七个参数，用于指定查询结果的排列方式，不执行则表示使用默认的排列方式。
            // 调用 query() 会返回一个 Cursor 对象，查询到的所有数据都将从这个对象中取出。
            val cursor = db.query("Book",null,null,null,null,null,null)
            // 将数据指针移动到第一行的位置
            if (cursor.moveToFirst()){
                do {
                    // 遍历 Cursor 对象，取出数据并打印
                    // getColumnIndex() 获取某一列在表中对应的位置索引
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))
                    Log.d(TAG,"book name is $name")
                    Log.d(TAG,"book author is $author")
                    Log.d(TAG,"book pages is $pages")
                    Log.d(TAG,"book price is $price")
                }while (cursor.moveToNext())
            }
            // 关闭 Cursor
            cursor.close()

            // SQL 语句
//            val cursor1 = db.rawQuery("select * from Book",null)
        }
    }

    private fun deleteData() {
        btnDelete.setOnClickListener{
            val db = dbHelper.writableDatabase
            // 删除页数超过 500 的数据，不指定会删除所有行
            db.delete("Book","pages>?", arrayOf("500"))

            // SQL 语句
//            db.execSQL("delete from Book where pages > ?", arrayOf("500"))

        }
    }

    private fun updateData() {
        btnUpdate.setOnClickListener{
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("price",10.99)
            // 第一个参数是表名
            // 第二个参数 ContentValues 对象
            // 第三、四个参数用于约束更新某一行或某几行中的数据，不指定的话默认会更新所有行。
            db.update("Book",values,"name=?", arrayOf("The Da Vinci Code"))

            // SQL 语句
//            db.execSQL("update Book set price = ? where name = ? ", arrayOf("10.99","The Da Vinci Code"))
        }
    }

    private fun insertData() {
        btnAdd.setOnClickListener{
            val db = dbHelper.writableDatabase
            val values1 = ContentValues().apply {
                // 开始组装第一条数据
                put("name","The Da Vinci Code")
                put("author","Dan Brown")
                put("pages",454)
                put("price",16.99)
            }
            // 插入第一条数据
            // 第一个参数是表名，
            // 第二个参数用于在未指定添加数据的情况下给某些可为空的列自动赋值 NULL，一般用不到这个功能，直接传入 null 即可，
            // 第三个参数是一个 ContentValues 对象，它提供了一系列的 put() 重载，用于向 ContentValues 中添加数据。
            db.insert("Book",null,values1)
            val values2 = ContentValues().apply {
                // 开始组装第一条数据
                put("name","The Da Lost Symbol")
                put("author","Dan Brown")
                put("pages",510)
                put("price",19.95)
            }
            // 插入第二条数据
            db.insert("Book",null,values2)

            // SQL 语句
//            db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
//                arrayOf("The Da Vinci Code","Dan Brown","454","16.69"))
//            db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
//                arrayOf("The Da Lost Symbol","Dan Brown","510","19.95"))

        }
    }

    /**
     * 创建成功之后，可将 BookStore.db 文件右键选择 Save AS，将它从模拟器导出到计算机上。
     * （BookStore.db-journal 文件是为了让数据库能够支持事务而产生的临时日志文件，通常大小为 0 字节，暂时不管它。）
     * 这里我没有使用 Database Navigator 插件，因为安装失败了。我使用的是 Navicat Premium。
     */
    private fun createTable() {
        // 第一次点击会创建数据库，再次点击则不会再执行 onCreate()，除非卸载程序重新安装。
        btnCreateDatabase.setOnClickListener{
            // 调用 getWritableDatabase()
            dbHelper.writableDatabase
        }
    }


    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, SQLiteActivity::class.java)
            context.startActivity(intent)
        }
    }
}