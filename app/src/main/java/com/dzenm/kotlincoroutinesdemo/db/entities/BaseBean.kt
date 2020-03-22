package com.dzenm.kotlincoroutinesdemo.db.entities

/**
 * 判断服务端数据返回的结果
 */
data class BaseBean<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)