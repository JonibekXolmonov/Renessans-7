package com.example.renessans7.ui.screen.teachermain

import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import kotlinx.coroutines.flow.MutableStateFlow

interface TeacherGroupViewModel {

    val groups: MutableStateFlow<UiStateObject<BaseResponse<List<Group>>>>

    fun getTeacherGroups()
}