package ru.itis.carssharing.ui.profile.main.news

import io.reactivex.functions.Consumer
import ru.itis.carssharing.model.data.feature.PersonFeature
import ru.itis.carssharing.ui.profile.main.ProfileFragment
import ru.itis.carssharing.util.ErrorHandler

class NewsListener(
    private val view: ProfileFragment,
    private val errorHandler: ErrorHandler
) : Consumer<PersonFeature.News> {

    override fun accept(news: PersonFeature.News) = when (news) {
        is PersonFeature.News.GetCurrentPersonSuccess -> {}
        is PersonFeature.News.GetCurrentPersonFailure -> errorHandler.proceed(news.throwable) { view.showError(it) }
    }
}