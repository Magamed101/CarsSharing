package ru.itis.carssharing.model.data.api

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Schedulers @Inject constructor() {

    fun io() = Schedulers.io()

    fun main() = AndroidSchedulers.mainThread()
}