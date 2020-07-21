package ru.itis.carssharing.ui.auth.register.vm

import ru.itis.carssharing.model.data.feature.AuthFeature

class ViewModel(
    val isLoading: Boolean
)

class ViewModelTransformer : (AuthFeature.State) -> ViewModel {
    override fun invoke(state: AuthFeature.State): ViewModel {
        return ViewModel(state.isLoading)
    }
}