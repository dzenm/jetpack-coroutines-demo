package com.dzenm.kotlincoroutinesdemo.mvvm.repository

import com.dzenm.kotlincoroutinesdemo.db.entities.BaseBean
import retrofit2.Call

interface IRepository {

    fun <T> getRepositoryService(
        tag: String,
        call: Call<BaseBean<T>>,
        onSuccess: (T) -> Unit
    )
}