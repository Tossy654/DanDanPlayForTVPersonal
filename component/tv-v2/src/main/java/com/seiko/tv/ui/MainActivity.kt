package com.seiko.tv.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commitNow
import androidx.navigation.fragment.NavHostFragment
import com.seiko.tv.R
import com.seiko.tv.ui.base.TvFragmentNavigator

class MainActivity : FragmentActivity() {

    companion object {
        private const val CONTAINER_ID = 0x1245
    }

    private lateinit var navigator: TvFragmentNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = FrameLayout(this)
        layout.id = CONTAINER_ID
        setContentView(layout)

        val fragment = NavHostFragment.create(0)
        supportFragmentManager.commitNow {
            replace(CONTAINER_ID, fragment)
        }

        navigator = TvFragmentNavigator(
            this,
            fragment.childFragmentManager,
            CONTAINER_ID
        )

        fragment.navController.navigatorProvider.addNavigator(navigator)
        fragment.navController.setGraph(R.navigation.tv_nav_base)

    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (navigator.dispatchKeyEvent(event)) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

}