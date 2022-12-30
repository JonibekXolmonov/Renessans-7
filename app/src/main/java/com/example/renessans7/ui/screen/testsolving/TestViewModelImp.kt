package com.example.renessans7.ui.screen.testsolving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.login.AuthResponse
import com.example.renessans7.models.test.Test
import com.example.renessans7.models.test.TestCheckRequest
import com.example.renessans7.models.test.TestCheckResponse
import com.example.renessans7.repository.AuthRepository
import com.example.renessans7.repository.TestRepository
import com.example.renessans7.utils.helper.UiStateList
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModelImp @Inject constructor(private val testRepository: TestRepository) :
    ViewModel(), TestViewModel {

    private val _test = MutableStateFlow<UiStateObject<BaseResponse<Test>>>(UiStateObject.EMPTY)
    override val test = _test

    private val _answers = MutableStateFlow<UiStateList<String>>(UiStateList.EMPTY)
    override val answers = _answers

    override fun saveAnswers(answers: List<String>) {
        viewModelScope.launch {
            _answers.value = UiStateList.SUCCESS(answers)
        }
    }

    override fun getTestById(testId: String) {
        _test.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _test.value = UiStateObject.SUCCESS(testRepository.getTestById(testId))
            } catch (e: Exception) {
                _test.value = UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun check(
        testCheckRequest: TestCheckRequest, block: (Result<BaseResponse<TestCheckResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(testRepository.check(testCheckRequest)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}