package ru.itis.carssharing.model.data.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.model.data.feature.PersonFeature.*
import ru.itis.carssharing.model.data.network.ApiService
import ru.itis.carssharing.model.data.storage.Preferences
import javax.inject.Inject

class PersonFeature @Inject constructor(
    apiService: ApiService,
    prefs: Preferences
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    actor = ActorImpl(apiService, prefs),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean = false,
        val rentedCars: ArrayList<Car> = arrayListOf()
    )

    sealed class Wish {
        object GetCurrentPersonWish : Wish()
    }

    sealed class Effect {
        object GetCurrentPersonStart : Effect()
        data class GetCurrentPersonSuccess(val list: ArrayList<Car>) : Effect()
        data class GetCurrentPersonFailure(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class GetCurrentPersonSuccess(val list: ArrayList<Car>) : News()
        data class GetCurrentPersonFailure(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val apiService: ApiService,
        private val prefs: Preferences
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.GetCurrentPersonWish -> {
                Observable.just(Effect.GetCurrentPersonSuccess(prefs.getRentedInArrayList()) as Effect)
                    .startWith(Effect.GetCurrentPersonStart)
                    .onErrorReturn { Effect.GetCurrentPersonFailure(it) }
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.GetCurrentPersonStart -> state.copy(isLoading = true)
            is Effect.GetCurrentPersonSuccess -> state.copy(isLoading = false, rentedCars = effect.list)
            is Effect.GetCurrentPersonFailure -> state.copy(isLoading = false)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.GetCurrentPersonSuccess -> News.GetCurrentPersonSuccess(effect.list)
            is Effect.GetCurrentPersonFailure -> News.GetCurrentPersonFailure(effect.throwable)

            else -> null
        }
    }
}
