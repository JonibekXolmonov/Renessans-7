package com.example.renessans7.ui.screen.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.login.AuthResponse
import com.example.renessans7.models.register.RegisterRequest
import com.example.renessans7.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImp @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), RegisterViewModel {

    override fun registerAsPupil(
        registerRequest: RegisterRequest,
        block: (Result<BaseResponse<AuthResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(authRepository.registerAsPupil(registerRequest)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }

    override fun registerAsTeacher(
        registerRequest: RegisterRequest,
        block: (Result<BaseResponse<AuthResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(authRepository.registerAsTeacher(registerRequest)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}