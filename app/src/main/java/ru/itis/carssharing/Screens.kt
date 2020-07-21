package ru.itis.carssharing

import ru.itis.carssharing.ui.auth.AuthFlowFragment
import ru.itis.carssharing.ui.auth.authorize.AuthorizeFragment
import ru.itis.carssharing.ui.auth.login.LogInFragment
import ru.itis.carssharing.ui.auth.register.RegisterFragment
import ru.itis.carssharing.ui.home.HomeFlowFragment
import ru.itis.carssharing.ui.home.list.HomeFragment
import ru.itis.carssharing.ui.main.MainFragment
import ru.itis.carssharing.ui.profile.ProfileFlowFragment
import ru.itis.carssharing.ui.profile.main.ProfileFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    //flows
    object AuthFlow : SupportAppScreen() {
        override fun getFragment() = AuthFlowFragment()
    }

    //tab flows
    object HomeFlow : SupportAppScreen() {
        override fun getFragment() = HomeFlowFragment()
    }

    object ProfileFlow : SupportAppScreen() {
        override fun getFragment() = ProfileFlowFragment()
    }

    //screens
    object Authorize : SupportAppScreen() {
        override fun getFragment() = AuthorizeFragment()
    }

    object LogIn : SupportAppScreen() {
        override fun getFragment() = LogInFragment()
    }

    object Register : SupportAppScreen() {
        override fun getFragment() = RegisterFragment()
    }

    data class Main(
        val currentTab: Int? = null
    ) : SupportAppScreen() {
        override fun getFragment() = MainFragment.create(currentTab)
    }

    object Home: SupportAppScreen() {
        override fun getFragment() = HomeFragment()
    }

    object Profile : SupportAppScreen() {
        override fun getFragment() = ProfileFragment()
    }

}