package ru.itis.carssharing.ui.auth.login.news

import io.reactivex.functions.Consumer
import ru.itis.carssharing.Constants
import ru.itis.carssharing.Screens
import ru.itis.carssharing.model.data.feature.AuthFeature
import ru.itis.carssharing.model.system.flow.FlowRouter
import ru.itis.carssharing.ui.auth.login.LogInFragment

class NewsListener(
    private val view: LogInFragment,
    private val flowRouter: FlowRouter
) : Consumer<AuthFeature.News> {

    override fun accept(news: AuthFeature.News) = when (news) {
        is AuthFeature.News.LogInSuccess -> flowRouter.newRootFlow(Screens.Main())
        is AuthFeature.News.LogInFailure -> view.showError(news.throwable.message ?: Constants.Error.BASE_ERROR)
        else -> {}
    }
}