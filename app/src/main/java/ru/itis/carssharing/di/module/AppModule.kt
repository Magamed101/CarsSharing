package ru.itis.carssharing.di.module

import android.content.Context
import com.google.gson.Gson
import ru.itis.carssharing.di.provider.GsonProvider
import ru.itis.carssharing.model.data.api.Schedulers
import ru.itis.carssharing.model.data.storage.Preferences
import ru.itis.carssharing.model.system.ResourceManager
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(
    context: Context
) : Module() {
    init {
        bind(Context::class.java).toInstance(context)
        bind(Gson::class.java).toProvider(GsonProvider::class.java).providesSingletonInScope()
        bind(ResourceManager::class.java).singletonInScope()
        bind(Preferences::class.java).singletonInScope()
        bind(Schedulers::class.java).singletonInScope()
        // navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}