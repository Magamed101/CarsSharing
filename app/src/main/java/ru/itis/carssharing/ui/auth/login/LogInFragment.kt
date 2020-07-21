package ru.itis.carssharing.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_login.*
import ru.itis.carssharing.R
import ru.itis.carssharing.entity.UserDto
import ru.itis.carssharing.model.data.feature.AuthFeature
import ru.itis.carssharing.model.system.flow.FlowRouter
import ru.itis.carssharing.ui.auth.login.events.UiEvents
import ru.itis.carssharing.ui.auth.login.events.UiEventsTransformer
import ru.itis.carssharing.ui.auth.login.news.NewsListener
import ru.itis.carssharing.ui.auth.register.vm.ViewModel
import ru.itis.carssharing.ui.auth.register.vm.ViewModelTransformer
import ru.itis.carssharing.ui.base.ObservableSourceFragment
import ru.itis.carssharing.util.setLoadingState
import javax.inject.Inject

class LogInFragment: ObservableSourceFragment<UiEvents>(), Consumer<ViewModel> {

    @Inject
    lateinit var flowRouter: FlowRouter

    fun provideRouter(): FlowRouter = scope.getInstance(FlowRouter::class.java)

    @Inject
    lateinit var feature: AuthFeature

    fun provideFeature(): AuthFeature = scope.getInstance(AuthFeature::class.java)

    override val layoutRes: Int
        get() = R.layout.fragment_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowRouter = provideRouter()
        feature = provideFeature()
        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatchers()
        setOnClickListeners()
    }

    private fun setUpBindings() {
        object : AndroidBindings<LogInFragment>(this) {
            override fun setup(view: LogInFragment) {
                binder.bind(feature to view using ViewModelTransformer())
                binder.bind(view to feature using UiEventsTransformer())
                binder.bind(feature.news to NewsListener(view, flowRouter))
            }
        }.setup(this)
    }

    private fun setOnClickListeners() {
        btn_login.setOnClickListener {
            onNext(
                UiEvents.OnBtnLogInClicked(
                UserDto(
                    et_login.text.toString(),
                    et_password.text.toString()
                )
            ))
        }
    }

    private fun addTextWatchers() {
        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(e: Editable?) {
                btn_login.isEnabled = et_login.text.toString().isNotEmpty()
                        && et_password.text.toString().length >= 8
            }
        }
        et_login.addTextChangedListener(textWatcher)
        et_password.addTextChangedListener(textWatcher)
    }

    override fun accept(vm: ViewModel) {
        pb_loading?.setLoadingState(vm.isLoading)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        flowRouter.exit()
    }

}