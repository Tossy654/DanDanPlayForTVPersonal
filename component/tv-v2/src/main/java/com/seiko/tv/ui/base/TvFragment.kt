package com.seiko.tv.ui.base

import android.view.KeyEvent

interface TvFragment {

    fun onFocused()

    fun onUnFocused()

    fun dispatchFocus(hasFocus: Boolean)

    fun isFocused(): Boolean

    fun dispatchKeyEvent(event: KeyEvent): Boolean

    fun onBackPressed(): Boolean

}