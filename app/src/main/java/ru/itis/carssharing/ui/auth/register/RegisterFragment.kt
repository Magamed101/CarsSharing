package ru.itis.carssharing.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_register.*
import ru.itis.carssharing.R
import ru.itis.carssharing.entity.UserDto
import ru.itis.carssharing.model.data.feature.AuthFeature
import ru.itis.carssharing.model.system.flow.FlowRouter
import ru.itis.carssharing.ui.auth.register.events.UiEvents
import ru.itis.carssharing.ui.auth.register.events.UiEventsTransformer
import ru.itis.carssharing.ui.auth.register.news.NewsListener
import ru.itis.carssharing.ui.auth.register.vm.ViewModel
import ru.itis.carssharing.ui.auth.register.vm.ViewModelTransformer
import ru.itis.carssharing.ui.base.ObservableSourceFragment
import ru.itis.carssharing.util.setLoadingState

class RegisterFragment : ObservableSourceFragment<UiEvents>(), Consumer<ViewModel> {

    private val flowRouter: FlowRouter by lazy {
        scope.getInstance(FlowRouter::class.java)
    }

    private val feature: AuthFeature by lazy {
        scope.getInstance(AuthFeature::class.java)
    }
    
    override val layoutRes: Int
        get() = R.layout.fragment_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBindings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatchers()
        setOnClickListeners()
    }

    private fun setUpBindings() {
        object : AndroidBindings<RegisterFragment>(this) {
            override fun setup(view: RegisterFragment) {
                binder.bind(feature to view using ViewModelTransformer())
                binder.bind(view to feature using UiEventsTransformer())
                binder.bind(feature.news to NewsListener(view, flowRouter))
            }
        }.setup(this)
    }

    private fun setOnClickListeners() {
        btn_register.setOnClickListener {
            onNext(
                UiEvents.OnBtnRegisterClicked(
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
                btn_register.isEnabled = et_login.text.toString().isNotEmpty()
                        && et_password.text.toString().length >= 8
                        && et_confirm_password.text.toString() == et_password.text.toString()
            }
        }
        et_login.addTextChangedListener(textWatcher)
        et_password.addTextChangedListener(textWatcher)
        et_confirm_password.addTextChangedListener(textWatcher)
    }

    override fun accept(vm: ViewModel) {
        pb_loading?.setLoadingState(vm.isLoading)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        flowRouter.exit()
    }

}