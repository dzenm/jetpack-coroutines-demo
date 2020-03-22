package com.dzenm.kotlincoroutinesdemo.mvvm.repository

import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean
import com.dzenm.kotlincoroutinesdemo.mvvm.bean.DataRepository
import com.dzenm.kotlincoroutinesdemo.mvvm.api.Api
import com.dzenm.kotlincoroutinesdemo.mvvm.bean.LoadingBean

class RepositoryHandler : AbsRepository<Api>() {

    override fun api(): Api = serviceData.createRetrofit(Api.BASE_URL).create(Api::class.java)

    fun loadMoreData() {
        getArticle()
    }

    fun deleteArticle(title: String) {
        localCache.deleteArticle(title)
    }

    fun getArticle(): DataRepository<ArticleBean> {
        getRepositoryService(
            "获取维保任务可转单用户列表",
            api().getArticle(requestPosition)
        ) {
            localCache.insertArticle(it.datas) {
                requestPosition++
                isRequestInProgress = false
                loadingLiveData.postValue(LoadingBean.SERVICE_FINISHED)
            }
        }
        return DataRepository(
            localCache.getArticle(),
            errorLiveData,
            loadingLiveData
        )
    }
}