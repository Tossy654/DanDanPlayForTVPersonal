package com.seiko.tv.navigation

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.seiko.tv.ui.base.TvFragment
import timber.log.Timber
import java.util.*

@Navigator.Name("tv_fragment")
class TvFragmentNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    companion object {
        private const val TAG = "TvFragmentNavigator"
        private const val KEY_BACK_STACK_IDS = "androidx-nav-fragment:navigator:backStackIds"
    }

    private val backStack = ArrayDeque<Int>()

    private var currentFragment: Fragment? = null

    override fun popBackStack(): Boolean {
        if (backStack.isEmpty()) {
            return false
        }

        if (manager.isStateSaved) {
            Timber.tag(TAG).i("Ignoring popBackStack() call: FragmentManager has already saved its state")
            return false
        }

        manager.popBackStack(
            generateBackStackName(backStack.size, backStack.peekLast()),
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        backStack.removeLast()
        return true
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Timber.tag(TAG).i("Ignoring navigate() call: FragmentManager has already saved its state")
            return null
        }

        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }

        val frag = instantiateFragment(context, manager, className, args)
        frag.arguments = args

        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        ft.replace(containerId, frag)
        ft.setPrimaryNavigationFragment(frag)
        currentFragment = frag

        val destId = destination.id
        val initialNavigation = backStack.isEmpty()
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && backStack.peekLast() == destId)

        val isAdded = when {
            initialNavigation -> {
                true
            }
            isSingleTopReplacement -> {
                if (backStack.size > 1) {
                    manager.popBackStack(
                        generateBackStackName(backStack.size, backStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(generateBackStackName(backStack.size, destId))
                }
                false
            }
            else -> {
                ft.addToBackStack(
                    generateBackStackName(backStack.size + 1, destId)
                )
                true
            }
        }

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()

        if (isAdded) {
            backStack.add(destId)
            return destination
        }
        return null
    }

    fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val tvFragment = currentFragment
        if (tvFragment is TvFragment && tvFragment.isFocused() && tvFragment.dispatchKeyEvent(event)) {
            return true
        }
        return false
    }

    override fun onSaveState(): Bundle? {
        val b = Bundle()
        val stack = IntArray(backStack.size)
        var index = 0
        for (id in backStack) {
            stack[index++] = id
        }
        b.putIntArray(KEY_BACK_STACK_IDS, stack)
        return b
    }

    override fun onRestoreState(savedState: Bundle?) {
        if (savedState != null) {
            val stack = savedState.getIntArray(KEY_BACK_STACK_IDS)
            if (stack != null) {
                backStack.clear()
                for (destId in stack) {
                    backStack.add(destId)
                }
            }
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int?): String {
        return "$backStackIndex-$destId"
    }

}