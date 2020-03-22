package com.dzenm.kotlincoroutinesdemo.mvvm.repository

import androidx.lifecycle.LiveData
import androidx.room.RoomDatabase
import com.dzenm.helper.log.Logger
import com.dzenm.kotlincoroutinesdemo.db.dao.LocalCacheDao
import com.dzenm.kotlincoroutinesdemo.db.database.AppDatabase
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean

/**
 * 构造方法填写[RoomDatabase]的[Dao]接口类, [RepositoryLocalCache]对数据库的数据进行操作,
 * 除了增删改查, 还可以添加逻辑操作, 进行一些中间量的处理, [RepositoryHandler] 中已经获取了
 * [RepositoryLocalCache] 的实例
 */
class RepositoryLocalCache private constructor(private val dao: LocalCacheDao) {

    fun insertArticle(articleBeans: List<ArticleBean>, insertFinished: () -> Unit) {
        logD("inserting ${articleBeans.size} article")
        dao.insert(articleBeans)
        insertFinished()
    }

    fun getArticle(): LiveData<List<ArticleBean>> {
        logD("query all article")
        return dao.get()
    }

    fun deleteArticle(title: String) {
        dao.delete(title)
    }

    private fun logD(msg: String) {
        Logger.d(TAG + msg)
    }

    companion object {
        private val TAG = RepositoryLocalCache::class.java.simpleName + "| "
        private var instance: RepositoryLocalCache? = null
        fun getInstance(): RepositoryLocalCache = instance
            ?: synchronized(this) {
                instance
                    ?: RepositoryLocalCache(AppDatabase.getInstance().localCacheDao())
                        .also { instance = it }
            }
    }
}