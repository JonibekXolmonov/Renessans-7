package com.example.renessans7.ui.screen.acceptrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.module.service.ApiService
import com.example.renessans7.repository.TeacherRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcceptToGroupViewModelImp @Inject constructor(private val teacherRepository: TeacherRepository) :
    ViewModel(), AcceptToGroupViewModel {

    private val _requests =
        MutableStateFlow<UiStateObject<BaseResponse<List<RequestsToJoin>>>>(UiStateObject.EMPTY)
    override val requests = _requests

    override fun getRequests() {
        _requests.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _requests.value = UiStateObject.SUCCESS(teacherRepository.getRequests())
            } catch (e: Exception) {
                _requests.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun accept(requestId: String, block: (Result<BaseResponse<Any>>) -> Unit) {
        viewModelScope.launch {
            try {
                block(Result.success(teacherRepository.accept(requestId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }

    override fun decline(requestId: String, block: (Result<BaseResponse<Any>>) -> Unit) {
        viewModelScope.launch {
            try {
                block(Result.success(teacherRepository.decline(requestId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}