package com.dzenm.kotlincoroutinesdemo.mvvm.bean

import com.dzenm.kotlincoroutinesdemo.mvvm.exception.ErrorConvert

/**
 * 请求相应的错误处理, 包括请求的名称, 错误的信息, 错误的类型
 */
data class ErrorBean(
    val errorTag: String,               // 错误tag,用于区别哪个请求出错
    val error: ErrorConvert.ResponseException        // 错误
)
