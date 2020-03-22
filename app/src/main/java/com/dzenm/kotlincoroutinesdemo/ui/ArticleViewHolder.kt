package com.dzenm.kotlincoroutinesdemo.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.kotlincoroutinesdemo.R
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.tv_title)
    private val type: TextView = view.findViewById(R.id.tv_type)
    private val author: TextView = view.findViewById(R.id.tv_author)
    private val date: TextView = view.findViewById(R.id.tv_date)

    private var articleBean: ArticleBean? = null

    init {
        view.setOnClickListener {
            articleBean?.link?.let { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(articleBean: ArticleBean?) {
        if (articleBean == null) {
            val resources = itemView.resources
            title.text = resources.getString(R.string.loading)
            type.text = resources.getString(R.string.loading)
            author.text = resources.getString(R.string.unknown)
            date.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(articleBean)
        }
    }

    private fun showRepoData(articleBean: ArticleBean) {
        this.articleBean = articleBean
        title.text = articleBean.title
        type.text = articleBean.chapterName
        author.text = articleBean.author
        date.text = articleBean.niceDate
    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
            return ArticleViewHolder(view)
        }
    }
}