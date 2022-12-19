package com.example.renessans7.ui.screen.addgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.models.group.Group
import com.example.renessans7.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModelImp @Inject constructor(private val teacherRepository: TeacherRepository) :
    ViewModel(), AddGroupViewModel {

    override fun addGroup(group: AddGroupRequest, block: (Result<BaseResponse<Group>>) -> Unit) {
        viewModelScope.launch {
            try {
                block(Result.success(teacherRepository.addGroup(group)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}