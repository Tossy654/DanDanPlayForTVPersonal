package com.seiko.tv.ui.base

import android.view.KeyEvent
import androidx.fragment.app.Fragment

abstract class TvBaseFragment : Fragment(), TvFragment {

    private var hasFocus = false

    override fun isFocused(): Boolean {
        return hasFocus
    }

    override fun onFocused() {
        hasFocus = true
    }

    override fun onUnFocused() {
        hasFocus = false
    }

    override fun dispatchFocus(hasFocus: Boolean) {
        if (view == null) {
            this.hasFocus = hasFocus
            return
        }
        if (hasFocus) {
            onFocused()
        } else {
            onUnFocused()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return false
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}