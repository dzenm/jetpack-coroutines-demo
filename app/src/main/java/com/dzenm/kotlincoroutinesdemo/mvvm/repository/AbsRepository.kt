package com.dzenm.kotlincoroutinesdemo.mvvm.repository

import androidx.lifecycle.MutableLiveData
import com.dzenm.kotlincoroutinesdemo.db.entities.BaseBean
import com.dzenm.kotlincoroutinesdemo.mvvm.bean.ErrorBean
import com.dzenm.kotlincoroutinesdemo.mvvm.bean.LoadingBean
import com.dzenm.kotlincoroutinesdemo.mvvm.exception.ErrorConvert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call

/**
 * <I>的类型为Retrofit请求的接口[api]
 */
abstract class AbsRepository<I> : IRepository {

    /**
     * 是否处于请求进行状态
     */
    protected var isRequestInProgress = false

    protected var requestPosition = 0

    protected val errorLiveData = MutableLiveData<ErrorBean>()

    protected val loadingLiveData = MutableLiveData<@LoadingBean.LoadingStatus Int>()

    protected val localCache: RepositoryLocalCache = RepositoryLocalCache.getInstance()

    protected val serviceData: RepositoryService = RepositoryService.getInstance()

    /**
     * 创建Retrofit请求的API接口, Retrofit的实例可以通过[serviceData]调用[createRetrofit]
     */
    abstract fun api(): I

    override fun <T> getRepositoryService(
        tag: String,
        call: Call<BaseBean<T>>,
        onSuccess: (T) -> Unit
    ) {
        serviceRepository(tag, call, onSuccess)
    }

    suspend fun <T> getRepositoryFromService(
        tag: String,
        call: Call<BaseBean<T>>,
        onSuccess: (T) -> Unit
    ) {
        requestServiceWithConvertData(tag, call, onSuccess)
    }

    /**
     * 在IO协程中进行网络数据请求, 将请求的结果返回并切换至主协程, 如果请求过程中出现了异常或后端返回错误, 将
     * 通过[errorLiveData]进行回调, 请求在开始和结束会通过[loadingLiveData]回调, [onSuccess]进行网络
     * 请求成功之后的自定义处理
     */
    private fun <T> serviceRepository(
        tag: String,
        call: Call<BaseBean<T>>,
        onSuccess: (T) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            requestServiceWithConvertData(
                tag,
                call,
                onSuccess
            )
        }
    }

    /**
     * 进行网络数据请求, 并处理请求发生的错误, 请求的状态, 请求成功的回调结果
     */
    private suspend fun <T> requestServiceWithConvertData(
        tag: String,
        call: Call<BaseBean<T>>,
        onSuccess: (T) -> Unit
    ) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        loadingLiveData.postValue(LoadingBean.SERVICE_PREPARE)
        serviceData.handler(
            tag,
            call,
            {
                val body = it.body()!!
                // 这个根据具体前后端接口协议来定
                if (body.data != null) {
                    onSuccess(body.data)
                    isRequestInProgress = false
                    loadingLiveData.postValue(LoadingBean.SERVICE_FINISHED)
                } else {
                    // 逻辑异常通过实际项目中具体制定的前后端接口文档来决定, 将异常结果发送出去
                    ErrorConvert.getInstance().exception(body.errorCode, body.errorMsg)
                        .run { errorLiveData.postValue(ErrorBean(tag, this)) }
                    isRequestInProgress = false
                    loadingLiveData.postValue(LoadingBean.SERVICE_FINISHED)
                }
            },
            { t, e ->
                errorLiveData.postValue(ErrorBean(t, e))
                isRequestInProgress = false
                loadingLiveData.postValue(LoadingBean.SERVICE_FINISHED)
            }
        )
    }
}