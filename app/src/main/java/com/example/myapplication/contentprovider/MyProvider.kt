package com.example.myapplication.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

/**
 * 很多方法里带有 uri 参数，这个参数也正是调用 ContentProvider 的增删改查方法时传递过来的。
 * 这里则需要对传入的 uri 参数进行解析，从中分析出调用方期望访问的表和数据。
 * ----------------------------------------------------------------------------------------------------
 * 标准的内容 URI 写法：表示调用方期望访问的是 com.example.myapplication 这个应用的 table1 表中的数据。
 * content://com.example.myapplication.provider/table1
 * 还可以在后面加上一个 id：表示调用方期望访问的是表中 id 为 1 的数据。
 * content://com.example.myapplication.provider/table1/1
 * ----------------------------------------------------------------------------------------------------
 * 内容 URI 的格式主要就只有以上两种，以路径结尾表示期望访问该表中所有数据，以 id 结尾表示期望访问该表中拥有相应 id 的数据。
 * 可以使用通配符分别匹配这两种格式的内容 URI，规则如下：
 * * 表示匹配任意长度的任意字符。
 * # 表示匹配任意长度的数字。
 * 所以，一个能够匹配任意表的内容 URI 格式如下：
 * content://com.example.myapplication.provider/星号
 * 一个能够匹配 table1 表中任意一行数据的内容 URI 格式如下：
 * content://com.example.myapplication.provider/table1/#
 * ----------------------------------------------------------------------------------------------------
 * 接着借助 UriMatcher 类实现匹配内容 URI 的功能，它提供了一个 addURI()，
 * 接收 3 个参数，可以分别把 authority、path 和一个自定义代码传进去。
 * 这样，当调用 UriMatcher 的 match() 时，就可以将一个 Uri 对象传入，返回值是某个能够匹配这个 Uri 对象所对应的自定义代码，
 * 利用这个代码，就可以判断出调用方期望访问的是哪张表中的数据了。
 * ----------------------------------------------------------------------------------------------------
 * 对于安全问题，因为所有的增删改查操作都一定要匹配到相应的内容 URI 格式才能进行，
 * 而我们当然不可能向 UriMatcher 中添加隐私数据的 URI，所以这部分数据根本无法被外部程序访问，安全问题也就不存在了。
 */

class MyProvider: ContentProvider() {

    private val tableDir = 0
    private val tableItem = 1
    private val table2Dir = 2
    private val table2Item = 3

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    /**
     * MyProvider 类实例化时，立刻创建了 UriMatcher 类的实例，并调用 addURI()，将期望匹配的内容 URI 格式传递进去。
     */
    init {
        uriMatcher.addURI("com.example.myapplication.provider","table",tableDir)
        uriMatcher.addURI("com.example.myapplication.provider","table/#",tableItem)
        uriMatcher.addURI("com.example.myapplication.provider","table2",table2Dir)
        uriMatcher.addURI("com.example.myapplication.provider","table2/#",table2Item)
    }

    /**
     * 查询的结果存放在 Cursor 对象中返回
     * uri 参数指定哪张表
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // 通过 match() 对传入的 Uri 对象进行匹配，
        // 如果发现 UriMatcher 中某个内容 URI 格式成功匹配了该 Uri 对象， 则会返回相应的自定义代码，
        // 然后就可以判断出调用方期望访问的数据了。
        // 其他方法的实现类似。
        when(uriMatcher.match(uri)){
            tableDir -> {
                // 查询 table1 表中的所有数据
            }
            tableItem -> {
                // 查询 table1 表中的单条数据
            }
            table2Dir -> {
                // 查询 table2 表中的所有数据
            }
            table2Item -> {
                // 查询 table2 表中的单条数据
            }
        }
        return null
    }

    /**
     * 添加完成后，返回一个用于表示这条新记录的 URI。
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    /**
     * 初始化 CP 时调用，通常用来完成对数据库的创建和升级等操作。
     * 返回 true 表示初始化成功，返回 false 表示失败。
     */
    override fun onCreate(): Boolean {
        return false
    }

    /**
     * 受影响的行数将作为返回值返回
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    /**
     * 被删除的行数将作为返回值返回
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    /**
     * 根据传入的内容 URI 返回相应的 MIME 类型。
     * 此方法是所有 CP 都必须提供的一个方法，用于获取 Uri 对象所对应的 MIME 类型。
     * 一个内容 URI 所对应的 MIME 字符串主要由 3 部分组成，Android 对这 3 个部分做了如下规定：
     * 必须以 vnd 开头。
     * 如果内容 URI 以路径结尾，则后接 android.cursor.dir/；如果内容 URI 以 id 结尾，则后接 android.cursor.item/。
     * 最后接上 vnd.<authority>.<path>。
     * -------------------------------------------------------------------------------------------------
     * content://com.example.myapplication.provider/table1 所对应的 MIME 类型为：
     * vnd.android.cursor.dir/vnd.com.example.myapplication.provider.table1
     * ------------------------------------------------------------------------------------------------
     * content://com.example.myapplication.provider/table1/1 所对应的 MIME 类型为：
     * vnd.android.cursor.item/vnd.com.example.myapplication.provider.table1
     */
    override fun getType(uri: Uri) = when(uriMatcher.match(uri)){
        tableDir -> "vnd.android.cursor.dir/vnd.com.example.myapplication.provider.table1"
        tableItem -> "vnd.android.cursor.item/vnd.com.example.myapplication.provider.table1"
        table2Dir -> "vnd.android.cursor.dir/vnd.com.example.myapplication.provider.table2"
        table2Item -> "vnd.android.cursor.item/vnd.com.example.myapplication.provider.table2"
        else -> null
    }
}