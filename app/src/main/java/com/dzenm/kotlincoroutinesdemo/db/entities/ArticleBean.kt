package com.dzenm.kotlincoroutinesdemo.db.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ArticleBean(
    @field:SerializedName("apkLink") var apkLink: String = "",
    @field:SerializedName("audit") var audit: Int = 0,
    @field:SerializedName("author") var author: String = "",
    @field:SerializedName("chapterId") var chapterId: Int = 0,
    @field:SerializedName("chapterName") var chapterName: String = "",
    @field:SerializedName("collect") var collect: Boolean = false,
    @field:SerializedName("courseId") var courseId: Int = 0,
    @field:SerializedName("desc") var desc: String = "",
    @field:SerializedName("envelopePic") var envelopePic: String = "",
    @field:SerializedName("fresh") var fresh: Boolean = false,
    @PrimaryKey
    @field:SerializedName("id") var id: Int = 0,
    @field:SerializedName("link") var link: String = "",
    @field:SerializedName("niceDate") var niceDate: String = "",
    @field:SerializedName("niceShareDate") var niceShareDate: String = "",
    @field:SerializedName("origin") var origin: String = "",
    @field:SerializedName("prefix") var prefix: String = "",
    @field:SerializedName("projectLink") var projectLink: String = "",
    @field:SerializedName("publishTime") var publishTime: Long = 0,
    @Ignore @field:SerializedName("shareDate") var shareDate: Any = "",
    @field:SerializedName("shareUser") var shareUser: String = "",
    @field:SerializedName("superChapterId") var superChapterId: Int = 0,
    @field:SerializedName("superChapterName") var superChapterName: String = "",
    @Ignore @field:SerializedName("tags") var tags: List<Any> = emptyList(),
    @field:SerializedName("title") var title: String = "",
    @field:SerializedName("type") var type: Int = 0,
    @field:SerializedName("userId") var userId: Int = 0,
    @field:SerializedName("visible") var visible: Int = 0,
    @field:SerializedName("zan") var zan: Int = 0
)