package ru.itis.carssharing.ui.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.mvicore.android.lifecycle.ResumePauseBinderLifecycle
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.modelWatcher
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_cars.*
import org.xml.sax.ErrorHandler
import ru.itis.carssharing.R
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.model.data.feature.CarsFeature
import ru.itis.carssharing.model.data.storage.Preferences
import ru.itis.carssharing.model.system.flow.FlowRouter
import ru.itis.carssharing.ui.base.MviFragment
import ru.itis.carssharing.ui.home.list.adapter.CarsAdapter
import ru.itis.carssharing.util.setGone
import ru.itis.carssharing.util.setVisible


class HomeFragment : MviFragment<HomeFragment.ViewModel, HomeFragment.UiEvents>() {

    override val layoutRes: Int
        get() = R.layout.fragment_cars

    private val prefs: Preferences by scope()
    private val feature: CarsFeature by scope()
    private val adapter: CarsAdapter by lazy {
        CarsAdapter(arrayListOf())
        { car ->
            with(MaterialAlertDialogBuilder(context)) {
                setTitle("Вы точно хотите арендовать машину?")
                setPositiveButton(android.R.string.ok) { _, _ ->
                    onNext(UiEvents.OnCarRented(car))
                }
                setNegativeButton(android.R.string.cancel) { _, _ ->  }
                show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initRecycler()
        setUpScreen()
    }

    override fun onResume() {
        super.onResume()
        onNext(UiEvents.OnStart)
    }

    private fun setUpScreen() {
        if (!prefs.admin) {
            iv_add.setGone()
        }
        else {
            iv_add.setVisible()
        }
    }

    override fun accept(vm: ViewModel) {
        modelWatcher<ViewModel> {
            watch(ViewModel::isLoading) { pb_loading.isVisible = it }
            watch(ViewModel::cars) { adapter.updateList(it) }
        }.invoke(vm)
    }

    private fun setUpBindings() {
        Binder(ResumePauseBinderLifecycle(lifecycle)).apply {
            bind(feature to this@HomeFragment using { state ->
                ViewModel(state.isLoading, state.cars) }
            )
            bind(this@HomeFragment to feature using { event ->
                when(event) {
                    is UiEvents.OnStart -> CarsFeature.Wish.GetCarsWish
                    is UiEvents.OnCarAdded -> CarsFeature.Wish.AddCarWish(event.car)
                    is UiEvents.OnCarRented -> CarsFeature.Wish.AddRentedCarWish(event.car)
                }
            })
        }
        feature.accept(CarsFeature.Wish.GetCarsWish)
    }

    private fun initRecycler() {
        rv_cars.layoutManager = LinearLayoutManager(context)
        rv_cars.adapter = adapter
    }

    private fun setOnClickListeners() {
        iv_add.setOnClickListener {
            with(MaterialAlertDialogBuilder(context)) {
                setTitle("Добавьте машину")
                val viewInflated = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_car, view as ViewGroup?, false)
                val et1 = viewInflated.findViewById<EditText>(R.id.et_name)
                val et2 = viewInflated.findViewById<EditText>(R.id.et_price)
                setView(viewInflated)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    onNext(UiEvents.OnCarAdded(Car(et1.text.toString(), et2.text.toString())))
                }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->  }
                    .show()
            }
        }
    }

    data class ViewModel(
        val isLoading: Boolean,
        val cars: ArrayList<Car>
    )

    sealed class UiEvents {
        object OnStart : UiEvents()
        data class OnCarAdded(val car: Car) : UiEvents()
        data class OnCarRented(val car: Car) : UiEvents()
    }

}
