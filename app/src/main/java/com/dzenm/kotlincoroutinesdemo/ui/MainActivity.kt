package com.dzenm.kotlincoroutinesdemo.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzenm.helper.base.AbsBaseActivity
import com.dzenm.helper.dialog.AbsDialogFragment
import com.dzenm.helper.dialog.InfoDialog
import com.dzenm.helper.toast.ToastHelper
import com.dzenm.kotlincoroutinesdemo.R
import com.dzenm.kotlincoroutinesdemo.db.entities.ArticleBean
import com.dzenm.kotlincoroutinesdemo.mvvm.bean.LoadingBean
import com.dzenm.kotlincoroutinesdemo.vm.FactoryHelper
import com.dzenm.kotlincoroutinesdemo.vm.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbsBaseActivity(), ArticleAdapter.OnLongItemClickListener {

    private lateinit var adapter: ArticleAdapter

    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(
            this,
            FactoryHelper.articleViewModelFactory()
        ).get(ArticleViewModel::class.java)

        setScrollLoadingMore(viewModel)
        adapter = ArticleAdapter()

        adapter.also {
            recycler_view.adapter = it
            ItemTouchHelper(TouchCallback(it))
                .attachToRecyclerView(recycler_view)
            it.onLongItemClickListener = this@MainActivity
        }

        // 通过LiveData监听数据改变实现对页面显示的改变
        viewModel.insert()
        viewModel.articleBeans.observe(this, Observer { article ->
            adapter.submitList(article)
        })
        viewModel.errors.observe(this, Observer { t ->
            ToastHelper.show(t.error.message)
        })
        viewModel.loading.observe(this, Observer {
            if (it == LoadingBean.LOAD_PREPARE) {
                show(true)
            } else if (it == LoadingBean.LOAD_FINISHED) {
                show(false)
            }
        })
    }

    private fun setScrollLoadingMore(viewModel: ArticleViewModel) {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastPosition == itemCount - 1) {
                        viewModel.loadMore()
                    }
                }
            }
        })
    }

    override fun onLongItemClick(articleBean: ArticleBean, position: Int) {
        InfoDialog.newInstance(this)
            .setTitle("删除提示")
            .setMessage("是否删除该任务?")
            .setOnDialogClickListener { _, confirm ->
                if (confirm) {
                    adapter.also {
                        val list = ArrayList<ArticleBean>(it.currentList)
                        viewModel.delete(list[position].title)
                        list.removeAt(position)
                        it.submitList(list)
                    }
                }
                return@setOnDialogClickListener true
            }.show<AbsDialogFragment>()
    }
}
