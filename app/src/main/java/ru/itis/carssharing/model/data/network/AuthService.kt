package ru.itis.carssharing.model.data.network

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import ru.itis.carssharing.Constants
import ru.itis.carssharing.entity.ErrorModel
import ru.itis.carssharing.entity.UserDto
import ru.itis.carssharing.model.data.api.Schedulers
import ru.itis.carssharing.model.data.storage.Preferences
import ru.itis.carssharing.util.getMd5Hash
import javax.inject.Inject

class AuthService @Inject constructor(
    private val prefs: Preferences,
    private val gson: Gson,
    private val schedulers: Schedulers
) {

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        .build()

    fun register(
        userDto: UserDto
    ): Observable<UserDto> {
        if (userDto.login == "admin123" && userDto.password == "admin123") {
            return Single.just(userDto)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doOnSuccess {
                    storeAccount(UserDto(
                        it.login,
                        it.password
                    ))
                    prefs.loggedIn = true
                    prefs.admin = true
                }
                .toObservable()
        }
        else if (true) {
            return Single.just(userDto)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doOnSuccess {
                    storeAccount(UserDto(
                        it.login,
                        it.password
                    ))
                    prefs.loggedIn = true
                    prefs.admin = false
                }
                .toObservable()
        }
        else {
            return Single.defer<UserDto> {
                val json = JSONObject()
                json.put("login", userDto.login)
                json.put("password", userDto.password.getMd5Hash())
                val body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    json.toString()
                )
                val request = Request.Builder()
                    .url(Constants.Api.BASE_URL + "auth/signup")
                    .post(body)
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val authModel =
                            gson.fromJson(response.body()?.charStream(), UserDto::class.java)
                        return@defer Single.just(authModel)
                    } else {
                        val resultCode = response.code()
                        val errorData =
                            gson.fromJson(response.body()?.charStream(), ErrorModel::class.java)
                        if (resultCode in 400..500) {
                            throw RuntimeException(errorData.error)
                        } else
                            throw RuntimeException("Get token data error: $resultCode")
                    }
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doOnSuccess {
                    storeAccount(
                        UserDto(
                            it.login,
                            it.password
                        )
                    )
                    prefs.loggedIn = true
                }
                .toObservable()
        }
    }

    fun logIn(
        userDto: UserDto
    ): Observable<UserDto> {
        if (userDto.login == "admin123" && userDto.password == "admin123") {
            return Single.just(userDto)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doOnSuccess {
                    storeAccount(UserDto(
                        it.login,
                        it.password
                    ))
                    prefs.loggedIn = true
                    prefs.admin = true
                }
                .toObservable()
        }
        else if (true) {
            return Single.just(userDto)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doOnSuccess {
                    storeAccount(UserDto(
                        it.login,
                        it.password
                    ))
                    prefs.loggedIn = true
                    prefs.admin = false
                }
                .toObservable()
        }
        else {
            return Single.defer<UserDto> {
                val json = JSONObject()
                json.put("login", userDto.login)
                json.put("password", userDto.password.getMd5Hash())
                val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
                val request = Request.Builder()
                    .url(Constants.Api.BASE_URL + "auth/login")
                    .post(body)
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val authModel = gson.fromJson(response.body()?.charStream(), UserDto::class.java)
                        return@defer Single.just(authModel)
                    } else {
                        val resultCode = response.code()
                        val errorData = gson.fromJson(response.body()?.charStream(), ErrorModel::class.java)

                        if (resultCode in 400..500) {
                            throw RuntimeException(errorData.error)
                        }
                        else
                            throw RuntimeException("Get token data error: $resultCode")
                    }
                } catch (e: RuntimeException) {
                    return@defer Single.error(e)
                }}
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doOnSuccess {
                    storeAccount(UserDto(
                        it.login,
                        it.password
                    ))
                    prefs.loggedIn = true
                }
                .toObservable()
        }
    }

    fun storeAccount(userDto: UserDto) {
        prefs.account = userDto
    }
}