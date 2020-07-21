package ru.itis.carssharing.ui.base

import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

abstract class MviFragment<IN, OUT> : BaseFragment(), ObservableSource<OUT>, Consumer<IN> {

    private val source = PublishSubject.create<OUT>()

    protected fun onNext(out: OUT) {
        source.onNext(out)
    }

    override fun subscribe(observer: Observer<in OUT>) {
        source.subscribe(observer)
    }

}