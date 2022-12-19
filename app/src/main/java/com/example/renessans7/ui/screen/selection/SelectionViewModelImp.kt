package com.example.renessans7.ui.screen.selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectionViewModelImp @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), SelectionViewModel {


    private val _teachers =
        MutableStateFlow<UiStateObject<BaseResponse<List<Teacher>>>>(UiStateObject.EMPTY)
    override val teachers = _teachers

    override fun getAllTeachers() {
        _teachers.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _teachers.value = UiStateObject.SUCCESS(authRepository.getAllTeachers())
            } catch (e: Exception) {
                _teachers.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun getTeacherGroups(
        teacherId: String,
        block: (Result<BaseResponse<List<Group>>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(authRepository.getTeacherGroups(teacherId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }

    override fun joinToGroup(classId: String, block: (Result<BaseResponse<Any>>) -> Unit) {
        viewModelScope.launch {
            try {
                block(Result.success(authRepository.joinToGroup(classId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}