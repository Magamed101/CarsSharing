package ru.itis.carssharing.model.data.network.error

import okhttp3.ResponseBody
import java.lang.RuntimeException

class ServerError(
    val code: Int,
    val responseBody: ResponseBody? = null
) : RuntimeException()