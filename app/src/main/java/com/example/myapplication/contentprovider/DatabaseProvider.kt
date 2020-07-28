package com.example.myapplication.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri
import com.example.myapplication.storage.MyDatabaseHelper

/**
 * 提供外部访问接口
 */
class DatabaseProvider : ContentProvider() {

    private val bookDir = 0
    private val bookItem = 1
    private val categoryDir = 2
    private val categoryItem = 3
    private val authority = "com.example.databasetest.provider"
    private var dbHelper: MyDatabaseHelper?=null

    /**
     * by lazy 代码块是 Kotlin 提供的一种懒加载技术，代码块中的代码一开始并不会执行，
     * 只有当 uriMatcher 变量首次被调用时才会执行，并且会将代码块中最后一行代码的返回值赋给 uriMatcher。
     */
    private val uriMatcher by lazy {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority,"book",bookDir)
        matcher.addURI(authority,"book/#",bookItem)
        matcher.addURI(authority,"category",categoryDir)
        matcher.addURI(authority,"category/#",categoryItem)
        matcher
    }

    /**
     * 综合利用了 Getter 方法语法糖、?. 操作符、let 函数、?: 操作符、以及单行代码函数语法糖。
     * 首先调用 getContext() 并借助 ?. 操作符和 let 函数判断它的返回值是否为空：
     * 如果为空就使用 ?: 操作符返回 false，表示 CP 初始化失败，如果不为空，就执行 let 函数中的代码。
     * ------------------------------------------------------------------------------
     * 在 let 函数中创建了一个 MyDatabaseHelper 实例，
     * 然后返回 true 表示 CP 初始化成功
     * ------------------------------------------------------------------------------
     * 由于是借助多个操作符和标准函数，因此这段逻辑是在一行表达式内完成的，符合单行代码函数的语法糖要求，
     * 所以直接用等号连接返回值即可。
     * ------------------------------------------------------------------------------
     * 其他几个方法的语法结构类似
     */
    override fun onCreate() = context?.let{

        dbHelper = MyDatabaseHelper(it, "BookStore.db", 2)
        true
    }?:false


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = dbHelper?.let {
        // 删除数据
        val db = it.readableDatabase
        val deletedRows = when(uriMatcher.match(uri)){
            bookDir -> db.delete("Book",selection,selectionArgs)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.delete("Book","id = ?", arrayOf(bookId))
            }
            categoryDir -> db.delete("Category",selection,selectionArgs)
            categoryItem -> {
                val categoryId = uri.pathSegments[1]
                db.delete("Category","id = ?", arrayOf(categoryId))
            }
            else -> 0
        }
        deletedRows
    }?: 0

    override fun insert(uri: Uri, values: ContentValues?) = dbHelper?.let {
        // 添加数据
        val db = it.writableDatabase
        val uriReturn = when(uriMatcher.match(uri)){
            bookDir,bookItem -> {
                val newBookId = db.insert("Book",null,values)
                Uri.parse("content://$authority/book/$newBookId")
            }
            categoryDir,categoryItem -> {
                val newBookId = db.insert("Category",null,values)
                Uri.parse("content://$authority/category/$newBookId")
            }
            else -> null
        }
        uriReturn
    }


    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?) = dbHelper?.let {
        // 查询数据
        val db = it.readableDatabase
        val cursor = when(uriMatcher.match(uri)){
            bookDir -> {
                db.query("Book",projection,selection,selectionArgs,null,null,sortOrder)
            }
            bookItem -> {
                // Uri 对象的 getPatchSegments()，它会将内容 URI 权限之后的部分以 "/" 符号进行分割，
                // 并把分割后的结果放入一个字符串列表中，这个列表的第 0 个位置存放的是路径，第 1 个位置是 id。
                val  bookId = uri.pathSegments[1]
                db.query("Book",projection,"id = ?", arrayOf(bookId),null,null,sortOrder)
            }
            categoryDir -> {
                db.query("Category",projection,selection,selectionArgs,null,null,sortOrder)
            }
            categoryItem -> {
                val  categoryId = uri.pathSegments[1]
                db.query("Category",projection,"id = ?", arrayOf(categoryId),null,null,sortOrder)
            }
            else -> null
        }
        cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?) = dbHelper?.let{
        // 更新数据
        val db = it.writableDatabase
        val updatedRows = when(uriMatcher.match(uri)){
            bookDir -> db.update("Book",values,selection,selectionArgs)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.update("Book",values,"id = ?", arrayOf(bookId))
            }
            categoryDir -> db.update("Category",values,selection,selectionArgs)
            categoryItem -> {
                val categoryId = uri.pathSegments[1]
                db.update("Category",values,"id = ?", arrayOf(categoryId))
            }
            else -> 0
        }
        updatedRows
    }?: 0


    override fun getType(uri: Uri) = when(uriMatcher.match(uri)){
        bookDir -> "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book"
        bookItem -> "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book"
        categoryDir -> "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category"
        categoryItem -> "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category"
        else -> null
    }
}


/**
 * 其它应用程序调用此接口的实例
 */
//class MainActivity : AppCompatActivity() {
//
//    var bookId: String? =null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        btnAdd.setOnClickListener{
//            // 添加数据
//            // 将内容 URI 解析成 Uri 对象
//            val uri = Uri.parse("content://com.example.databasetest.provider/book")
//            // 将要添加的数据存放在 ContentValues 对象中
//            val values = contentValuesOf("name" to "A Clash of Kings",
//                "author" to "George Martin","pages" to 1040,"price" to 22.85)
//            val newUri = contentResolver.insert(uri,values)
//            // insert() 会返回一个 Uri 对象，其中包含了新增数据的 id。
//            bookId = newUri?.pathSegments?.get(1)
//        }
//        btnQuery.setOnClickListener{
//            // 查询数据
//            val uri = Uri.parse("content://com.example.databasetest.provider/book")
//            contentResolver.query(uri,null,null,null,null)?.apply {
//                while (moveToNext()){
//                    val name = getString(getColumnIndex("name"))
//                    val author = getString(getColumnIndex("author"))
//                    val pages = getInt(getColumnIndex("pages"))
//                    val price = getDouble(getColumnIndex("price"))
//                    Log.e("TAG","name is $name")
//                    Log.e("TAG","author is $author")
//                    Log.e("TAG","pages is $pages")
//                    Log.e("TAG","price is $price")
//                }
//                close()
//            }
//        }
//        btnUpdate.setOnClickListener{
//            // 更新数据
//            // 在尾部增加了一个 id，避免影响其他行
//            bookId?.let {
//                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")
//                val values = contentValuesOf("name" to "A Storm of Swords",
//                    "pages" to 1216,"price" to 24.05)
//                contentResolver.update(uri,values,null,null)
//            }
//        }
//        btnDelete.setOnClickListener{
//            // 删除数据
//            // 在尾部增加了一个 id，避免影响其他行
//            bookId?.let {
//                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")
//                contentResolver.delete(uri,null,null)
//            }
//        }
//    }
//}