package com.dzenm.kotlincoroutinesdemo.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzenm.kotlincoroutinesdemo.mvvm.repository.RepositoryHandler
import com.dzenm.kotlincoroutinesdemo.vm.viewmodel.ArticleViewModel

class FactoryHelper(
    private val handler: RepositoryHandler,
    private val position: Int
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (position) {
        ARTICLE -> ArticleViewModel(handler) as T
        else -> ArticleViewModel(handler) as T
    }

    companion object {

        const val ARTICLE = 1

        fun articleViewModelFactory(): FactoryHelper = FactoryHelper(RepositoryHandler(), ARTICLE)
    }
}