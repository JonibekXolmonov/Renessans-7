package com.example.renessans7.ui.screen.grouppupils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.repository.TeacherRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupPupilsViewModelImp @Inject constructor(private val teacherRepository: TeacherRepository) :
    ViewModel(), GroupPupilsViewModel {

    private val _pupils =
        MutableStateFlow<UiStateObject<BaseResponse<com.example.renessans7.models.pupils.Class>>>(
            UiStateObject.EMPTY
        )
    override val pupils = _pupils

    override fun getGroupPupils(classId: String) {
        _pupils.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _pupils.value = UiStateObject.SUCCESS(teacherRepository.getGroupPupils(classId))
            } catch (e: Exception) {
                _pupils.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun removePupilFromGroup(
        classId: String,
        pupilId: String,
        block: (Result<BaseResponse<Any>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(teacherRepository.removePupilFromGroup(classId, pupilId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}