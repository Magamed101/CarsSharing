package ru.itis.carssharing.util

import retrofit2.HttpException
import ru.itis.carssharing.R
import ru.itis.carssharing.Screens
import ru.itis.carssharing.model.data.network.error.ServerError
import ru.itis.carssharing.model.data.storage.Preferences
import ru.itis.carssharing.model.system.ResourceManager
import ru.itis.carssharing.model.system.flow.FlowRouter
import java.io.IOException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val resourceManager: ResourceManager,
    private val prefs: Preferences,
    private val flowRouter: FlowRouter
) {

    fun proceed(error: Throwable, messageListener: (String) -> Unit = {}) {
        error.printStackTrace()
        messageListener(
            when (error) {
                is ServerError -> when (error.code) {
                    401 -> { logout(); error.userMessage(resourceManager)}
                    else -> error.userMessage(resourceManager)
                }
                is HttpException -> resourceManager.getString(R.string.server_error)
                is IOException -> resourceManager.getString(R.string.connection_error)
                else -> resourceManager.getString(R.string.unknown_error)
            }
        )
    }

    private fun logout() {
        prefs.removeSharedPreferences()
        prefs.loggedIn = false
        flowRouter.newRootScreen(Screens.AuthFlow)
    }

}