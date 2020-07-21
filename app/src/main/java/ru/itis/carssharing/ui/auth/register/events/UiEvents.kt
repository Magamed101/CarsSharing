package ru.itis.carssharing.ui.auth.register.events

import ru.itis.carssharing.entity.UserDto
import ru.itis.carssharing.model.data.feature.AuthFeature

sealed class UiEvents {
    data class OnBtnRegisterClicked(val userAuthDto: UserDto) : UiEvents()
}

class UiEventsTransformer : (UiEvents) -> AuthFeature.Wish {
    override fun invoke(event: UiEvents) = when (event) {
        is UiEvents.OnBtnRegisterClicked -> AuthFeature.Wish.RegisterWish(event.userAuthDto)
    }
}