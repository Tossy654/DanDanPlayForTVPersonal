package com.seiko.tv.ui.widget

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.BaseGridView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class TvListViewHolder(
    view: View,
    private val scrollState: TvViewHolderScrollState
) : RecyclerView.ViewHolder(view), TvViewHolder {

    companion object {
        const val TAG = "TvListViewHolder"
    }

    private lateinit var recyclerView: TvRecyclerView
    private var isFocused = false

    abstract fun getScrollKey(): String?

    abstract fun getRecyclerView(): BaseGridView

    override fun onCreated() {
        if (itemView is ViewGroup) {
            itemView.clipChildren = false
            itemView.clipToPadding = false
        }
        itemView.isFocusable = true
        itemView.isFocusableInTouchMode = true
        itemView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                getRecyclerView().requestFocus()
                onFocused()
            }
        }
        recyclerView = getRecyclerView() as TvRecyclerView
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                scrollState.save(getScrollKey(), getRecyclerView())
            }
            override fun onViewAttachedToWindow(v: View?) {
            }
        })
        applyFocusUiState(false)
    }

    override fun onPreBound() {
        scrollState.save(getScrollKey(), getRecyclerView())
//        applyFocusUiState(false)
    }

    override fun onBound() {
        scrollState.restore(getScrollKey(), getRecyclerView())
    }

    override fun onRecycled() {
        // No-op
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return recyclerView.dispatchKeyEvent(event,
            consumeEdgeLeft = false,
            consumeEdgeTop = false,
            consumeEdgeRight = true, // Consume the event if we've reached the end of the list
            consumeEdgeBottom = false)
    }

    override fun onFocused() {
        Timber.tag(TAG).d("Nested list at %d gained focus", adapterPosition)
        applyFocusUiState(true)
        isFocused = true
    }

    override fun onUnFocused() {
        Timber.tag(TAG).d("Nested list at %d lost focus", adapterPosition)
        applyFocusUiState(false)
        isFocused = false
    }

    override fun hasFocus(): Boolean {
        return isFocused
    }

    open fun applyFocusUiState(hasFocus: Boolean) {
        getRecyclerView().alpha = if (hasFocus) 1.0f else 0.15f
    }

}