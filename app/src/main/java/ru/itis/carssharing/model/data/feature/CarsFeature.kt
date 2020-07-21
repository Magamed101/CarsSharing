package ru.itis.carssharing.model.data.feature

import android.os.Handler
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.model.data.feature.CarsFeature.*
import ru.itis.carssharing.model.data.storage.Preferences
import javax.inject.Inject

class CarsFeature @Inject constructor(
    prefs: Preferences
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    actor = ActorImpl(prefs),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean = false,
        val cars: ArrayList<Car> = arrayListOf()
    )

    sealed class Wish {
        object GetCarsWish : Wish()
        data class AddCarWish(val car: Car) : Wish()
        data class AddRentedCarWish(val car: Car) : Wish()
    }

    sealed class Effect {
        object GetCarsStart : Effect()
        data class GetCarsSuccess(val list: ArrayList<Car>) : Effect()
        data class GetCarsFailure(val throwable: Throwable) : Effect()

        object AddCarStart : Effect()
        data class AddCarSuccess(val list: ArrayList<Car>) : Effect()
        data class AddCarFailure(val throwable: Throwable) : Effect()

        object AddRentedCarStart : Effect()
        data class AddRentedCarSuccess(val list: ArrayList<Car>) : Effect()
        data class AddRentedCarFailure(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class GetCarsSuccess(val list: ArrayList<Car>) : News()
        data class GetCarsFailure(val throwable: Throwable) : News()

        data class AddCarSuccess(val msg: String) : News()
        data class AddCarFailure(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val prefs: Preferences
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.GetCarsWish -> {
                Observable.just(Effect.GetCarsSuccess(prefs.getDataInArrayList()) as Effect)
                    .startWith(Effect.GetCarsStart)
                    .onErrorReturn { Effect.GetCarsFailure(it) }
            }

            is Wish.AddCarWish -> {
                Handler().postDelayed({}, 1000)
                prefs.setDataInList(
                        prefs.getDataInArrayList().apply {
                            add(
                                Car(
                                    wish.car.name,
                                    wish.car.price
                                )
                            )
                        }
                    )
                Observable.just(Effect.AddCarSuccess(prefs.getDataInArrayList()) as Effect)
                    .startWith(Effect.AddCarStart)
                    .onErrorReturn { Effect.AddCarFailure(it) }
            }

            is Wish.AddRentedCarWish -> {
                prefs.setRentedInList(
                    prefs.getRentedInArrayList().apply {
                        add(
                            Car(
                                wish.car.name,
                                wish.car.price
                            )
                        )
                    }
                )
                prefs.setDataInList(
                    prefs.getDataInArrayList().apply {
                        remove(
                            Car(
                                wish.car.name,
                                wish.car.price
                            )
                        )
                    }
                )
                Observable.just(Effect.AddRentedCarSuccess(prefs.getDataInArrayList()) as Effect)
                    .startWith(Effect.AddRentedCarStart)
                    .onErrorReturn { Effect.AddRentedCarFailure(it) }
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.GetCarsStart -> state.copy(isLoading = true)
            is Effect.GetCarsSuccess -> state.copy(isLoading = false, cars = effect.list)
            is Effect.GetCarsFailure -> state.copy(isLoading = false)

            is Effect.AddCarStart -> state.copy(isLoading = true)
            is Effect.AddCarSuccess -> state.copy(isLoading = false, cars = effect.list)
            is Effect.AddCarFailure -> state.copy(isLoading = false)

            is Effect.AddRentedCarStart -> state.copy(isLoading = true)
            is Effect.AddRentedCarSuccess -> state.copy(isLoading = false, cars = effect.list)
            is Effect.AddRentedCarFailure -> state.copy(isLoading = false)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.GetCarsSuccess -> News.GetCarsSuccess(effect.list)
            is Effect.GetCarsFailure -> News.GetCarsFailure(effect.throwable)

            is Effect.AddRentedCarSuccess -> News.AddCarSuccess("Машина арендована")

            else -> null
        }
    }
}
