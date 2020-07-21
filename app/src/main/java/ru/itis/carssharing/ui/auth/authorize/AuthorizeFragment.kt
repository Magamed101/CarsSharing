package ru.itis.carssharing.ui.auth.authorize

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_authorize.*
import ru.itis.carssharing.R
import ru.itis.carssharing.Screens
import ru.itis.carssharing.model.system.flow.FlowRouter
import ru.itis.carssharing.ui.base.BaseFragment

class AuthorizeFragment : BaseFragment() {

    private val flowRouter: FlowRouter by lazy {
        scope.getInstance(FlowRouter::class.java)
    }

    override val layoutRes: Int
        get() = R.layout.fragment_authorize

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        btn_login?.setOnClickListener { goToLogin() }
        tv_sign_up?.setOnClickListener{ goToRegister()}
    }

    private fun goToLogin() = flowRouter.navigateTo(Screens.LogIn)

    private fun goToRegister() = flowRouter.navigateTo(Screens.Register)

    override fun onBackPressed() {
        super.onBackPressed()
        flowRouter.exit()
    }
}