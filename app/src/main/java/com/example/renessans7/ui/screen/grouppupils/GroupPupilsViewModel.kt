package com.example.renessans7.ui.screen.grouppupils

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface GroupPupilsViewModel {

    val pupils: MutableStateFlow<UiStateObject<BaseResponse<com.example.renessans7.models.pupils.Class>>>

    fun getGroupPupils(classId: String)

    fun removePupilFromGroup(
        classId: String,
        pupilId: String,
        block: (Result<BaseResponse<Any>>) -> Unit
    )

}