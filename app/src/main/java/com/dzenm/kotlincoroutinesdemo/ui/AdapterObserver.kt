package com.dzenm.kotlincoroutinesdemo.ui

import androidx.recyclerview.widget.RecyclerView

class AdapterObserver(var recyclerView: RecyclerView) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
    }
}