package ru.itis.carssharing.ui.main

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_main.*
import ru.itis.carssharing.R
import ru.itis.carssharing.Screens
import ru.itis.carssharing.ui.base.BaseFragment
import ru.itis.carssharing.ui.base.FlowFragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFragment : BaseFragment() {

    companion object {
        fun create(
            currentTab: Int? = null
        ) =
            MainFragment().apply {
                arguments = bundleOf(
                    EXTRA_CURRENT_TAB to currentTab
                )
            }

        private const val EXTRA_CURRENT_TAB = "EXTRA_CURRENT_TAB"
    }

    private val router: Router by scope()

    private var currentTab: Int? = null

    private val homeTab by lazy { Screens.HomeFlow }
    private val profileTab by lazy { Screens.ProfileFlow }

    override val layoutRes: Int
        get() = R.layout.fragment_main

    private val currentTabFragment: FlowFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? FlowFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBottomNavigation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(EXTRA_CURRENT_TAB) == true) {
            currentTab = arguments!!.getInt(EXTRA_CURRENT_TAB)
        }
    }

    private fun initBottomNavigation() {
        bnv_main.itemIconTintList = null
        bnv_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> selectTab(homeTab)
                R.id.profile -> selectTab(profileTab)
                else -> selectTab(homeTab)
            }
            true
        }
        bnv_main.itemIconTintList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(R.color.black, R.color.mhBlue)
        )
        bnv_main.selectedItemId = currentTab ?: R.id.home
        selectTab(
            when (currentTabFragment?.tag) {
                homeTab.screenKey -> homeTab
                profileTab.screenKey -> profileTab
                else -> homeTab
            }
        )
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)
        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return
        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                R.id.mainScreenContainer,
                createTabFragment(tab),
                tab.screenKey
            )
            currentFragment?.let {
                hide(it)
                it.userVisibleHint = false
            }
            newFragment?.let {
                show(it)
                it.userVisibleHint = true
            }
        }.commitNow()
    }

    private fun createTabFragment(tab: SupportAppScreen) = tab.fragment

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }

}