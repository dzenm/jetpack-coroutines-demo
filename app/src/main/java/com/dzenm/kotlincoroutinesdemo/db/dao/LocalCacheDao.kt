package com.dzenm.kotlincoroutinesdemo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean

@Dao
interface LocalCacheDao {

    @Query("select * from articleBean order by niceDate desc")
    fun get(): LiveData<List<ArticleBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleBean: List<ArticleBean>)

    @Query("delete from articleBean where title = :title ")
    fun delete(title: String)
}