package com.dzenm.kotlincoroutinesdemo.mvvm.bean

import androidx.lifecycle.LiveData

data class DataRepository<T>(
    val data: LiveData<List<T>>,
    val error: LiveData<ErrorBean>,
    val loadStatus: LiveData<Int>
)