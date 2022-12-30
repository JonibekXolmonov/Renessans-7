package com.example.renessans7.ui.screen.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.repository.AdminRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModelImp @Inject constructor(private val adminRepository: AdminRepository) :
    ViewModel(), AdminViewModel {

    private val _teachers =
        MutableStateFlow<UiStateObject<BaseResponse<List<Teacher>>>>(UiStateObject.EMPTY)
    override val teachers = _teachers

    private val _pupils =
        MutableStateFlow<UiStateObject<BaseResponse<List<Pupil>>>>(UiStateObject.EMPTY)
    override val pupils = _pupils

    override fun getAllPupils() {
        _pupils.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _pupils.value = UiStateObject.SUCCESS(adminRepository.getAllPupils())
            } catch (e: Exception) {
                _pupils.value = UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun getTeachers() {
        _teachers.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _teachers.value = UiStateObject.SUCCESS(adminRepository.getTeachers())
            } catch (e: Exception) {
                _teachers.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun removeUser(userId: String, block: (Result<BaseResponse<Any>>) -> Unit) {
        viewModelScope.launch {
            try {
                block(Result.success(adminRepository.removeUser(userId = userId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}