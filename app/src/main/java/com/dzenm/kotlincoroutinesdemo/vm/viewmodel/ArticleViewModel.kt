package com.dzenm.kotlincoroutinesdemo.vm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean
import com.dzenm.kotlincoroutinesdemo.mvvm.bean.ErrorBean
import com.dzenm.kotlincoroutinesdemo.mvvm.repository.RepositoryHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArticleViewModel(private val handler: RepositoryHandler) : ViewModel() {

    private val liveData = MutableLiveData<String>()

    private val articleResult = Transformations.map(liveData) {
        handler.getArticle()
    }

    val articleBeans: LiveData<List<ArticleBean>> =
        Transformations.switchMap(articleResult) { it.data }
    val errors: LiveData<ErrorBean> = Transformations.switchMap(articleResult) { it.error }
    val loading: LiveData<Int> = Transformations.switchMap(articleResult) { it.loadStatus }


    fun insert() {
        liveData.postValue("")
    }

    fun delete(title: String) {
        GlobalScope.launch {
            handler.deleteArticle(title)
        }
    }

    fun loadMore() {
        handler.loadMoreData()
    }
}