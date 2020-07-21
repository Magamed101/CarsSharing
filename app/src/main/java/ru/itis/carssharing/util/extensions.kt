package ru.itis.carssharing.util

import retrofit2.HttpException
import ru.itis.carssharing.R
import ru.itis.carssharing.model.system.ResourceManager
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace
import java.io.IOException

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun Navigator.setLaunchScreen(screen: SupportAppScreen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

fun Throwable.userMessage(resourceManager: ResourceManager) = when (this) {
    is HttpException -> when (this.code()) {
        401 -> resourceManager.getString(R.string.unauthorized_error)
        404 -> resourceManager.getString(R.string.not_found_error)
        500 -> resourceManager.getString(R.string.server_error)
        else -> resourceManager.getString(R.string.unknown_error)
    }
    is IOException -> resourceManager.getString(R.string.connection_error)
    else -> resourceManager.getString(R.string.unknown_error)
}
