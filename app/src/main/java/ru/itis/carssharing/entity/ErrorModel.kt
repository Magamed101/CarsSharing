package ru.itis.carssharing.entity

data class ErrorResponse(
    val errors: List<ErrorModel>
)

data class ErrorModel(
    val error: String
)