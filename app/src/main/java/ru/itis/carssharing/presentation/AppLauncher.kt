package ru.itis.carssharing.presentation

import ru.itis.carssharing.Screens
import ru.itis.carssharing.di.DI
import ru.itis.carssharing.di.module.ServerModule
import ru.itis.carssharing.model.data.storage.Preferences
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import javax.inject.Inject

class AppLauncher @Inject constructor(
    private val router: Router,
    private val prefs: Preferences
) {

    fun initModules() {
        if (!Toothpick.isScopeOpen(DI.SERVER_SCOPE)) {
            Toothpick.openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
                .installModules(ServerModule())
        }
    }

    fun coldStart() {
        if (prefs.loggedIn) {
            router.newRootScreen(Screens.Main())
        }
        else {
            router.newRootScreen(Screens.AuthFlow)
        }
    }

}