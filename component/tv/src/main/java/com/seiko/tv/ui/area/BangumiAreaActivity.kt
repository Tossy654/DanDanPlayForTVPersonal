package com.seiko.tv.ui.area

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.seiko.tv.R
import com.seiko.tv.util.removeWindowInTransitionManager
import com.seiko.tv.util.setupSharedElementTransition

class BangumiAreaActivity : FragmentActivity(R.layout.activity_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSharedElementTransition()
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) == null) {
            val fragment = BangumiAreaFragmentV2.newInstance()
            supportFragmentManager.commit {
                add(R.id.container, fragment, FRAGMENT_TAG)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeWindowInTransitionManager()
    }

    companion object {
        private const val FRAGMENT_TAG = "BangumiAreaActivity"

        fun launch(activity: Activity) {
            val intent = Intent(activity, BangumiAreaActivity::class.java)
            activity.startActivity(intent)
        }
    }

}