package com.example.renessans7.ui.screen.login

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.login.AuthResponse

interface LoginViewModel {

    fun login(loginRequest: LoginRequest, block: (Result<BaseResponse<AuthResponse>>) -> Unit)

}