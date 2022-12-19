package com.example.renessans7.models

data class BaseResponse<T>(
    val data: T,
    val message: String,
    val success: Boolean
)