package ru.itis.carssharing.model.data.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import ru.itis.carssharing.entity.UserDto
import ru.itis.carssharing.model.data.feature.AuthFeature.*
import ru.itis.carssharing.model.data.network.AuthService
import javax.inject.Inject

class AuthFeature @Inject constructor(
    authService: AuthService
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    actor = ActorImpl(authService),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean = false
    )

    sealed class Wish {
        data class RegisterWish(val userAuthDto: UserDto) : Wish()
        data class LogInWish(val userAuthDto: UserDto) : Wish()
    }

    sealed class Effect {
        object RegisterStart : Effect()
        data class RegisterSuccess(val userDto: UserDto) : Effect()
        data class RegisterFailure(val throwable: Throwable) : Effect()

        object LogInStart : Effect()
        object LogInSuccess : Effect()
        data class LogInFailure(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class RegisterSuccess(val userDto: UserDto) : News()
        data class RegisterFailure(val throwable: Throwable) : News()

        object LogInSuccess : News()
        data class LogInFailure(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val authService: AuthService
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.RegisterWish -> {
                authService.register(wish.userAuthDto)
                    .map { Effect.RegisterSuccess(it) as Effect }
                    .startWith(Effect.RegisterStart)
                    .onErrorReturn { Effect.RegisterFailure(it) }
            }

            is Wish.LogInWish -> {
                authService.logIn(wish.userAuthDto)
                    .map { Effect.LogInSuccess as Effect}
                    .startWith(Effect.LogInStart)
                    .onErrorReturn { Effect.LogInFailure(it) }
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.RegisterStart -> state.copy(isLoading = true)
            is Effect.RegisterSuccess -> state.copy(isLoading = false)
            is Effect.RegisterFailure -> state.copy(isLoading = false)

            is Effect.LogInStart -> state.copy(isLoading = true)
            is Effect.LogInSuccess -> state.copy(isLoading = false)
            is Effect.LogInFailure -> state.copy(isLoading = false)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.RegisterSuccess -> News.RegisterSuccess(effect.userDto)
            is Effect.RegisterFailure -> News.RegisterFailure(effect.throwable)

            is Effect.LogInSuccess -> News.LogInSuccess
            is Effect.LogInFailure -> News.LogInFailure(effect.throwable)

            else -> null
        }
    }
}
