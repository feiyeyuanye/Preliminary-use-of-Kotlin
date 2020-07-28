package com.example.myapplication.network

interface HttpCallbackListener {

    /**
     * 服务器成功响应请求时的回调
     * @param response 服务器返回的数据
     */
    fun onFinish(response:String)

    /**
     * 网络操作出现错误时调用
     * @param e 错误的详细信息
     */
    fun onError(e:Exception)

}