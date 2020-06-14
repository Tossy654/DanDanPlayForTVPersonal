package com.seiko.tv.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import androidx.leanback.widget.VerticalGridView

class TvVerticalRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : VerticalGridView(context, attrs), TvRecyclerView, View.OnFocusChangeListener {

    private val delegate = TvRecyclerViewDelegate(this)
    private var columns = 1
    private val focusListeners = arrayListOf<OnFocusChangeListener>()

    init {
        clipChildren = false
        clipToPadding = false
        onFocusChangeListener = this
        setScrollDirections()
    }

    fun setScrollDirections(
        scrollLeft: Boolean = false,
        scrollTop: Boolean = true,
        scrollRight: Boolean = false,
        scrollBottom: Boolean = true
    ) {
        delegate.setScrollDirections(scrollLeft, scrollTop, scrollRight, scrollBottom)
    }

    override fun addFocusListener(listener: OnFocusChangeListener) {
        focusListeners.add(listener)
    }

    override fun removeFocusListener(listener: OnFocusChangeListener) {
        focusListeners.remove(listener)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            selectCurrentPosition()
        }
        for (listener in focusListeners) {
            listener.onFocusChange(v, hasFocus)
        }
    }

    /**
     * Allows passing optional arguments to [dispatchKeyEvent]
     */
    fun dispatchKeyEventAndConsumeEdges(
        event: KeyEvent,
        consumeEdgeLeft: Boolean = false,
        consumeEdgeTop: Boolean = true,
        consumeEdgeRight: Boolean = false,
        consumeEdgeBottom: Boolean = true
    ): Boolean {
        return delegate.dispatchKeyEvent(
            event,
            consumeEdgeLeft,
            consumeEdgeTop,
            consumeEdgeRight,
            consumeEdgeBottom
        )
    }

    override fun dispatchKeyEvent(
        event: KeyEvent,
        consumeEdgeLeft: Boolean,
        consumeEdgeTop: Boolean,
        consumeEdgeRight: Boolean,
        consumeEdgeBottom: Boolean
    ): Boolean {
        return delegate.dispatchKeyEvent(
            event,
            consumeEdgeLeft,
            consumeEdgeTop,
            consumeEdgeRight,
            consumeEdgeBottom
        )
    }

    override fun getCurrentViewHolder(): ViewHolder? {
        return delegate.getCurrentViewHolder()
    }

    override fun setNumColumns(numColumns: Int) {
        super.setNumColumns(numColumns)
        columns = numColumns
        if (columns > 1) {
            setScrollDirections(
                scrollLeft = true,
                scrollTop = true,
                scrollRight = true,
                scrollBottom = true
            )
        }
    }

    fun getNumberOfColumns(): Int = columns

    fun selectCurrentPosition() {
        delegate.selectCurrentPosition()
    }

    fun unSelectCurrentPosition() {
        delegate.unSelectCurrentPosition()
    }

}