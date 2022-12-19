package com.example.renessans7.ui.screen.teachermain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import com.example.renessans7.repository.AuthRepository
import com.example.renessans7.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherGroupViewModelImp @Inject constructor(private val teacherRepository: TeacherRepository) :
    ViewModel(), TeacherGroupViewModel {


    private val _groups =
        MutableStateFlow<UiStateObject<BaseResponse<List<Group>>>>(UiStateObject.EMPTY)
    override val groups = _groups

    override fun getTeacherGroups() {
        _groups.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _groups.value = UiStateObject.SUCCESS(teacherRepository.getTeacherGroups())
            } catch (e: Exception) {
                _groups.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }
}