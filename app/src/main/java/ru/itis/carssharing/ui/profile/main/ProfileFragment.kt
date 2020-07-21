package ru.itis.carssharing.ui.profile.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.itis.carssharing.R
import ru.itis.carssharing.Screens
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.model.data.feature.PersonFeature
import ru.itis.carssharing.model.data.storage.Preferences
import ru.itis.carssharing.model.system.flow.FlowRouter
import ru.itis.carssharing.ui.base.ObservableSourceFragment
import ru.itis.carssharing.ui.home.list.adapter.CarsAdapter
import ru.itis.carssharing.ui.profile.main.news.NewsListener
import ru.itis.carssharing.util.ErrorHandler
import ru.itis.carssharing.util.setLoadingState

class ProfileFragment : ObservableSourceFragment<ProfileFragment.UiEvents>(), Consumer<ProfileFragment.ViewModel> {

    override val layoutRes: Int
        get() = R.layout.fragment_profile

    private val feature: PersonFeature by scope()
    private val errorHandler: ErrorHandler by scope()
    private val prefs: Preferences by scope()
    private val router: FlowRouter by scope()
    private val adapter: CarsAdapter by lazy {
        CarsAdapter(arrayListOf()) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initRecycler()
    }

    override fun onResume() {
        super.onResume()
        setUpScreen()
        onNext(UiEvents.OnStart)
    }

    private fun initRecycler() {
        rv_rented_cars.layoutManager = LinearLayoutManager(context)
        rv_rented_cars.adapter = adapter
    }

    sealed class UiEvents {
        object OnStart : UiEvents()
    }

    class ViewModel(
        val isLoading: Boolean,
        val rentedCars: ArrayList<Car>
    )

    private fun setUpBindings() {
        object : AndroidBindings<ProfileFragment>(this) {
            override fun setup(view: ProfileFragment) {
                binder.bind(feature to view using object : (PersonFeature.State) -> ViewModel {
                    override fun invoke(state: PersonFeature.State): ViewModel {
                        return ViewModel(
                            state.isLoading,
                            state.rentedCars
                        )
                    }
                })
                binder.bind(view to feature using object : (UiEvents) -> PersonFeature.Wish {
                    override fun invoke(event: UiEvents): PersonFeature.Wish = when (event) {
                        is UiEvents.OnStart -> PersonFeature.Wish.GetCurrentPersonWish
                    }
                })
                binder.bind(feature.news to NewsListener(
                    view,
                    errorHandler
                )
                )
            }
        }.setup(this)
        feature.accept(PersonFeature.Wish.GetCurrentPersonWish)
    }

    private fun setUpScreen() {
        val account = prefs.account
        tv_name?.text = account.login
    }

    private fun setOnClickListeners() {
        iv_exit.setOnClickListener {
            prefs.removeSharedPreferences()
            prefs.loggedIn = false
            router.newRootFlow(Screens.AuthFlow)
        }
        swipe_container.setOnRefreshListener {
            val runnable = Runnable {
                onNext(UiEvents.OnStart)
                swipe_container.isRefreshing = false
            }
            Handler().postDelayed(
                runnable, 3000.toLong()
            )
        }
        swipe_container.setOnClickListener {
            onNext(UiEvents.OnStart)
        }
    }

    override fun accept(vm: ViewModel) {
        pb_loading?.setLoadingState(vm.isLoading)
        vm.rentedCars.let { adapter.updateList(it) }
    }

}