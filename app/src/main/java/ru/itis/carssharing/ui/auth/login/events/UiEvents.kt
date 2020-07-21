package ru.itis.carssharing.ui.auth.login.events

import ru.itis.carssharing.entity.UserDto
import ru.itis.carssharing.model.data.feature.AuthFeature

sealed class UiEvents {
    data class OnBtnLogInClicked(val userAuthDto: UserDto) : UiEvents()
}

class UiEventsTransformer : (UiEvents) -> AuthFeature.Wish {
    override fun invoke(event: UiEvents): AuthFeature.Wish = when (event) {
        is UiEvents.OnBtnLogInClicked -> AuthFeature.Wish.LogInWish(event.userAuthDto)
    }
}