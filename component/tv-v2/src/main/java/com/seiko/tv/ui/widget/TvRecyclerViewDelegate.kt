package com.seiko.tv.ui.widget

import android.view.KeyEvent
import android.view.SoundEffectConstants
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class TvRecyclerViewDelegate(
    private val recyclerView: BaseGridView
) {

    companion object {
        const val TAG = "TvRecyclerView"
    }

    private var currentViewHolder: RecyclerView.ViewHolder? = null
    private var layoutManager = recyclerView.layoutManager!!

    private var isScrolling = false
    private var scrollLeftAllowed = false
    private var scrollUpAllowed = false
    private var scrollRightAllowed = false
    private var scrollDownAllowed = false
    private var pendingSelection = false

    init {
        recyclerView.addOnChildViewHolderSelectedListener(object : OnChildViewHolderSelectedListener() {
            override fun onChildViewHolderSelected(
                parent: RecyclerView?,
                child: RecyclerView.ViewHolder?,
                position: Int,
                subposition: Int
            ) {
                super.onChildViewHolderSelected(parent, child, position, subposition)
                currentViewHolder = child
                if (child is TvViewHolder
                    && ((recyclerView.hasFocus() && !child.hasFocus()) || pendingSelection)) {
                    child.itemView.requestFocus()
                    pendingSelection = false
                }
            }
        })
    }

    fun selectCurrentPosition() {
        val viewHolder = getCurrentViewHolder()
        if (viewHolder is TvViewHolder) {
            viewHolder.itemView.requestFocus()
        } else {
            pendingSelection = true
        }
    }

    fun unSelectCurrentPosition() {
        val viewHolder = getCurrentViewHolder()
        if (viewHolder is TvViewHolder) {
            viewHolder.onUnFocused()
        }
    }

    /**
     * 设置允许移动的方向
     */
    fun setScrollDirections(
        scrollLeft: Boolean,
        scrollUp: Boolean,
        scrollRight: Boolean,
        scrollDown: Boolean
    ) {
        scrollLeftAllowed = scrollLeft
        scrollUpAllowed = scrollUp
        scrollRightAllowed = scrollRight
        scrollDownAllowed = scrollDown
    }

    fun dispatchKeyEvent(
        event: KeyEvent,
        consumeEdgeLeft: Boolean,
        consumeEdgeTop: Boolean,
        consumeEdgeRight: Boolean,
        consumeEdgeBottom: Boolean
    ): Boolean {
        val vh = currentViewHolder

        // 当前ViewHolder是否可以自己消费掉 key event
        if (vh is TvViewHolder && vh.dispatchKeyEvent(event)) {
            return true
        }

        val isScrollAllowed = (scrollLeftAllowed && event.isLeft())
                || (scrollUpAllowed && event.isUp())
                || (scrollRightAllowed && event.isRight())
                || (scrollDownAllowed && event.isDown())


        if (!isScrollAllowed) {
            return false
        }

        if (isScrolling && event.isBeingReleased()) {
            isScrolling = false
            return true
        }

        // 忽略非移动的按键
        if (!event.isBeingPressed() || !isScrollEvent(event)) {
            return false
        }

        val previousViewHolder = currentViewHolder
        val previousView = previousViewHolder?.itemView

        val nextView = findNextView(event)
        if (nextView == null) {
            isScrolling = false
            return false
        }

        val nextViewHolder = recyclerView.findContainingViewHolder(nextView)

        if (nextViewHolder is TvViewHolder && !nextView.hasFocus()) {
            nextView.requestFocus()
            Timber.tag(TAG).d("Dispatching focus to: %d", nextViewHolder.adapterPosition)
            nextView.playSoundEffect(SoundEffectConstants.CLICK)
        }

        if (previousViewHolder is TvViewHolder
            && nextViewHolder != previousViewHolder
            && previousViewHolder.hasFocus()
        ) {
            Timber.tag(TAG).d("Clearing focus to: %d", previousViewHolder.adapterPosition)
            previousViewHolder.onUnFocused()
        }

        isScrolling = checkIsScrolling(nextView, previousView)
        currentViewHolder = nextViewHolder

        if (!isScrolling) {
            val consumeEdge = (consumeEdgeLeft && event.isLeft())
                    || (consumeEdgeTop && event.isUp())
                    || (consumeEdgeRight && event.isRight())
                    || (consumeEdgeBottom && event.isDown())
            if (consumeEdge) {
                return true
            }
            Timber.tag(TAG).d("Didn't scroll")
            return false
        }
        return isScrolling
    }

    private fun checkIsScrolling(nextView: View, currentView: View?): Boolean {
        if (nextView != currentView) {
            return true
        }
        return recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE
                || layoutManager.isSmoothScrolling
    }

    fun getCurrentViewHolder(): RecyclerView.ViewHolder? {
        if (currentViewHolder == null
            || currentViewHolder?.adapterPosition == RecyclerView.NO_POSITION
        ) {
            currentViewHolder = recyclerView.findViewHolderForAdapterPosition(
                recyclerView.selectedPosition
            )
        }
        return currentViewHolder
    }

    private fun findNextView(event: KeyEvent): View? {
        val nextView = recyclerView.focusSearch(
            layoutManager.findViewByPosition(recyclerView.selectedPosition),
            getFocusDirection(event)
        )
        if (nextView.parent != recyclerView) {
            return null
        }
        return nextView
    }

    private fun getFocusDirection(event: KeyEvent): Int {
        return when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> View.FOCUS_LEFT
            KeyEvent.KEYCODE_DPAD_UP -> View.FOCUS_UP
            KeyEvent.KEYCODE_DPAD_RIGHT -> View.FOCUS_RIGHT
            KeyEvent.KEYCODE_DPAD_DOWN -> View.FOCUS_DOWN
            else -> 0
        }
    }

    private fun isScrollEvent(event: KeyEvent): Boolean {
        return event.isLeft() || event.isUp() || event.isRight() || event.isDown()
    }
}