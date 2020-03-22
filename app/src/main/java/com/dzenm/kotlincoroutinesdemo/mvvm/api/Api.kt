package com.dzenm.kotlincoroutinesdemo.mvvm.api

import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean
import com.dzenm.kotlincoroutinesdemo.db.entities.BaseBean
import com.dzenm.kotlincoroutinesdemo.db.entities.Page
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @GET("article/list/{page}/json")
    fun getArticleAsync(@Path("page") page: Int): Deferred<ArticleBean>

    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page: Int): Call<BaseBean<Page<ArticleBean>>>

}