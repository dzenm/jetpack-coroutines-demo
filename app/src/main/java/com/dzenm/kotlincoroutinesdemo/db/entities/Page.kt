package com.dzenm.kotlincoroutinesdemo.db.entities

/**
 * 分页数据
 */
data class Page<T>(
    val curPage: Int,
    val datas: MutableList<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)