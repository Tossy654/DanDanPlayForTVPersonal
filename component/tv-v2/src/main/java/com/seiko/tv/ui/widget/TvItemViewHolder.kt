package com.seiko.tv.ui.widget

import android.view.KeyEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.seiko.tv.R
import timber.log.Timber

abstract class TvItemViewHolder(view: View): RecyclerView.ViewHolder(view), TvViewHolder {

    companion object {
        const val TAG = "TvItemViewHolder"
        private const val DEFAULT_FOCUS_ANIMATION_DURATION = 200L
        private const val DEFAULT_UN_FOCUS_ANIMATION_DURATION = 500L
        private const val FOCUS_SCALE_SIZE = 1.1f
    }

    private val focusAnimationTranslationZ: Float = view.resources
        .getDimensionPixelSize(R.dimen.tv_item_spacing).toFloat()

    private val focusAnimationInterpolator = DecelerateInterpolator()
    private val unFocusAnimationInterpolator = DecelerateInterpolator()
    private val focusAnimationDuration = DEFAULT_FOCUS_ANIMATION_DURATION
    private val unFocusAnimationDuration = DEFAULT_UN_FOCUS_ANIMATION_DURATION

    override fun onCreated() {
        itemView.isFocusable = true
        itemView.isFocusableInTouchMode = true
        itemView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                onUnFocused()
            } else {
                onFocused()
            }
        }
    }

    override fun onDetach() {
        // No-op
    }

    override fun onBound() {
        itemView.scaleX = 1.0f
        itemView.scaleY = 1.0f
    }

    override fun onRecycled() {
        // No-op
        // Clear resources if needed
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return false
    }

    override fun onFocused() {
        Timber.tag(TAG).d("Item at %d gained focus", adapterPosition)
        itemView.animate()
            .scaleX(FOCUS_SCALE_SIZE)
            .scaleY(FOCUS_SCALE_SIZE)
            .translationZ(focusAnimationTranslationZ)
            .setInterpolator(focusAnimationInterpolator)
            .duration = focusAnimationDuration
    }

    override fun onUnFocused() {
        Timber.tag(TAG).d("Item at %d lost focus", adapterPosition)
        itemView.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .translationZ(0f)
            .setInterpolator(unFocusAnimationInterpolator)
            .duration = unFocusAnimationDuration
    }

    override fun hasFocus(): Boolean {
        return itemView.hasFocus()
    }
}