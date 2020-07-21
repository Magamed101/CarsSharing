package ru.itis.carssharing.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.itis.carssharing.di.DI
import ru.itis.carssharing.ui.AppActivity
import ru.itis.carssharing.util.objectScopeName
import toothpick.Scope
import toothpick.Toothpick
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val STATE_SCOPE_NAME = "state_scope_name"

abstract class BaseFragment: Fragment() {
    abstract val layoutRes: Int

    private var instanceStateSaved: Boolean = false

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName ?: DI.SERVER_SCOPE
    }

    lateinit var fragmentScopeName: String
    lateinit var scope: Scope
        private set

    protected open fun installModules(scope: Scope) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()

        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            scope = Toothpick.openScope(fragmentScopeName)
        } else {
            scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            installModules(scope)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutRes, container, false)

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            Toothpick.closeScope(scope.name)
        }
    }

    fun isRealRemoving(): Boolean =
        (isRemoving && !instanceStateSaved) ||
                ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    inline fun <reified T> scope() =
        object : ReadWriteProperty<BaseFragment, T> {
            private var value: Any? = null
            override fun setValue(thisRef: BaseFragment, property: KProperty<*>, value: T) {
                this.value = value
            }
            override fun getValue(thisRef: BaseFragment, property: KProperty<*>): T {
                if (value == null) {
                    val instance = thisRef.scope.getInstance(T::class.java)
                    check(instance != null && instance is T) { "Property is null or has different class type" }
                    value = instance
                }
                return value as T
            }
        }

    fun showError(message: String) {
        (activity as? AppActivity)?.showError(message)
    }

    fun showSuccess(message: String) {
        (activity as? AppActivity)?.showSuccess(message)
    }

    open fun onBackPressed() {}
}