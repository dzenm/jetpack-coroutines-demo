package com.dzenm.kotlincoroutinesdemo.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean
import com.dzenm.kotlincoroutinesdemo.db.dao.LocalCacheDao
import com.dzenm.kotlincoroutinesdemo.ui.App

@Database(entities = [ArticleBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun localCacheDao(): LocalCacheDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        App.app
                    ).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Database.db"
            ).allowMainThreadQueries().build()
    }
}