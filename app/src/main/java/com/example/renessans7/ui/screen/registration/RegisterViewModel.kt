package com.example.renessans7.ui.screen.registration

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.login.AuthResponse
import com.example.renessans7.models.register.RegisterRequest

interface RegisterViewModel {

    fun registerAsTeacher(
        registerRequest: RegisterRequest,
        block: (Result<BaseResponse<AuthResponse>>) -> Unit
    )


    fun registerAsPupil(
        registerRequest: RegisterRequest,
        block: (Result<BaseResponse<AuthResponse>>) -> Unit
    )

}