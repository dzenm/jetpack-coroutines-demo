package com.dzenm.kotlincoroutinesdemo.mvvm.bean

import androidx.annotation.IntDef

object LoadingBean {

    const val LOAD_PREPARE = 0
    const val SERVICE_PREPARE = 1
    const val SERVICE_FINISHED = 2
    const val LOCAL_CACHE_PREPARE = 3
    const val LOCAL_CACHE_FINISHED = 4
    const val LOAD_FINISHED = 5

    @IntDef(SERVICE_PREPARE, SERVICE_FINISHED, LOCAL_CACHE_PREPARE, LOCAL_CACHE_FINISHED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class LoadingStatus
}
