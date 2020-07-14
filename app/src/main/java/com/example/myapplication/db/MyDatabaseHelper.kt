package com.example.myapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name:String, version:Int):
        SQLiteOpenHelper(context,name,null,version){

    /**
     * integer 表示整型
     * real 表示浮点型
     * text 表示文本类型
     * blob 表示二进制类型
     *
     * primary key 将 id 列设为主键，并用 autoincrement 关键字表示 id 列是自增长的。
     *
     * 第二版
     */
//    private val createBook = "create table Book(" +
//            "id integer primary key autoincrement," +
//            "author text," +
//            "price real," +
//            "pages integer," +
//            "name text)"
    /**
     * 第三版，新加字段，用来和 Category 表建立关联。
     */
    private val createBook = "create table Book(" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text," +
            "category_id integer)"

    private val createCategory = "create table Category(" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createBook)
        db.execSQL(createCategory)
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show()
    }


    /**
     * 升级数据库的最佳写法
     *
     * 注意，每当升级一个数据库版本时，onUpgrade() 里都一定要写一个相应的 if 判断语句，保证 App 在跨版本升级时，每一次的数据库修改都能被全部执行。
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("TAG","oldVersion is $oldVersion")
        // 现版本 version 为 3，oldVersion 为 2.
        // 如果直接安装最新版程序，则会走 onCreate()，不会走 onUpgrade()。
        // 如果是从 version 2 升级为 version 3 版本，则会走 onUpgrade()，并且执行下面的语句来升级数据库。

        // 当用户直接安装第二版程序时，就会进入 onCreate()，将两张表一起创建，
        // 如果用户使用第二版程序覆盖第一版的程序时，就会进入升级数据库的操作中，只需创建一个表就可以。
        if (oldVersion <= 1){
            db.execSQL(createCategory)
        }

        if (oldVersion <= 2){
            db.execSQL("alter table Book add column category_id integer")
        }


        // 暴力写法，开发阶段还可以，上线不行。
        // 执行了两条 DROP 语句，如果发现数据库中已经存在 Book 表或 Category 表，就将它们删除，然后调用 onCreate() 重新创建。
//        db.execSQL("drop table if exists Book")
//        db.execSQL("drop table if exists Category")
//        onCreate(db)
    }
}