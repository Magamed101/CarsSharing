package ru.itis.carssharing.model.data.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.entity.UserDto
import javax.inject.Inject

class Preferences @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

    private fun getSharedPreferences(prefsName: String) =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun removeSharedPreferences() =
        context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

    private val AUTH_DATA = "auth_data"
    private val APP_DATA = "app_data" // не очищается при выходе
    private val KEY_ACCOUNT = "ad_user"
    private val KEY_LOGGED_IN = "logged_in"
    private val KEY_ADMIN = "admin"
    private val KEY_CARS = "cars"
//    private val KEY_RENTED_CARS = "rented_cars"

    private val authPrefs by lazy { getSharedPreferences(AUTH_DATA) }
    private val appPrefs by lazy { getSharedPreferences(APP_DATA) }

    var account: UserDto
        get() = gson.fromJson(authPrefs.getString(KEY_ACCOUNT, null), UserDto::class.java)
        set(value) = authPrefs.edit().putString(KEY_ACCOUNT, gson.toJson(value)).apply()

    var loggedIn: Boolean
        get() = appPrefs.getBoolean(KEY_LOGGED_IN, false)
        set(value) = appPrefs.edit().putBoolean(KEY_LOGGED_IN, value).apply()

    var admin: Boolean
        get() = authPrefs.getBoolean(KEY_ADMIN, false)
        set(value) = authPrefs.edit().putBoolean(KEY_ADMIN, value).apply()

    var cars: String
        get() {
            val json = appPrefs.getString(KEY_CARS, null)
            val type = object : TypeToken<ArrayList<Car>>() {}.type
            return gson.fromJson(json, type)
        }
        set(value) = appPrefs.edit().putString(KEY_CARS, gson.toJson(value)).apply()

    fun setDataInList(list: ArrayList<Car>) {
        val json = gson.toJson(list)
        appPrefs.edit().putString(KEY_CARS, json).apply()
    }

    fun getDataInArrayList(): ArrayList<Car> {
        val json = appPrefs.getString(KEY_CARS, null)
        val type = object : TypeToken<ArrayList<Car>>() {}.type
        return gson.fromJson<ArrayList<Car>>(json ?: "[]", type)
    }

    fun setRentedInList(list: ArrayList<Car>) {
        val json = gson.toJson(list)
        appPrefs.edit().putString(account.login, json).apply()
    }

    fun getRentedInArrayList(): ArrayList<Car> {
        val json = appPrefs.getString(account.login, null)
        val type = object : TypeToken<ArrayList<Car>>() {}.type
        return gson.fromJson<ArrayList<Car>>(json ?: "[]", type)
    }
}