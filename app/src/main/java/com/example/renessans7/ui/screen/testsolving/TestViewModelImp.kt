package com.example.renessans7.ui.screen.testsolving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.login.AuthResponse
import com.example.renessans7.repository.AuthRepository
import com.example.renessans7.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModelImp @Inject constructor(private val testRepository: TestRepository) :
    ViewModel(), TestViewModel {



}