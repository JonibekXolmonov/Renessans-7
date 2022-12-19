package com.example.renessans7.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.login.AuthResponse
import com.example.renessans7.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImp @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), LoginViewModel {
    override fun login(
        loginRequest: LoginRequest,
        block: (Result<BaseResponse<AuthResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(authRepository.login(loginRequest)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}