package ru.itis.carssharing.model.data.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.itis.carssharing.entity.BaseResponse
import ru.itis.carssharing.entity.Car
import ru.itis.carssharing.entity.UserDto

interface ApiService {

    @POST("auth/signup")
    fun signUp(
        @Body userDto: UserDto
    ): Single<BaseResponse<UserDto>>

    @POST("auth/login")
    fun logIn(
        @Body userDto: UserDto
    ): Single<BaseResponse<UserDto>>

    @POST("auth/register")
    fun register(
        @Body userDto: UserDto
    ): Single<BaseResponse<UserDto>>

    @GET("cars")
    fun getAvailableCars(
    ): Observable<BaseResponse<List<Car>>>

    @POST("cars/rented")
    fun getRentedCars(
        @Body userDto: UserDto
    ): Observable<BaseResponse<List<Car>>>

}