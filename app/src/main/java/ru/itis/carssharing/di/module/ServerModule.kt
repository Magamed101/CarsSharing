package ru.itis.carssharing.di.module

import okhttp3.OkHttpClient
import ru.itis.carssharing.di.provider.ApiProvider
import ru.itis.carssharing.di.provider.OkHttpClientProvider
import ru.itis.carssharing.model.data.network.ApiService
import toothpick.config.Module

class ServerModule : Module() {
    init {
        // Network
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(ApiService::class.java).toProvider(ApiProvider::class.java).providesSingletonInScope()
    }

}