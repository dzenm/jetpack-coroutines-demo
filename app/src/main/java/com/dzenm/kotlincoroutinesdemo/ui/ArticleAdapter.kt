package com.dzenm.kotlincoroutinesdemo.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean
import java.util.*
import kotlin.collections.ArrayList

class ArticleAdapter : ListAdapter<ArticleBean, ArticleViewHolder>(ARTICLE_COMPARATOR),
    TouchCallback.OnTouchMoveListener {

    var onLongItemClickListener: OnLongItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.itemView.setOnLongClickListener { _ ->
                onLongItemClickListener?.onLongItemClick(it, position)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val list = ArrayList<ArticleBean>(currentList)
        if (fromPosition < toPosition) {                 // 数据交换
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
                submitList(list)
            }
        } else {
            for (i in fromPosition downTo toPosition) {
                Collections.swap(list, i, i - 1)
                submitList(list)
            }
        }
    }

    override fun onItemRemoved(position: Int) {
        getItem(position)?.also {
            onLongItemClickListener?.onLongItemClick(it, position)
        }
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<ArticleBean>() {
            override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean =
                oldItem.title == newItem.title
        }
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(articleBean: ArticleBean, position: Int)
    }

}