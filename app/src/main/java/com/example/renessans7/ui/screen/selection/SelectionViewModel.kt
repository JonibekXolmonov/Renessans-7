package com.example.renessans7.ui.screen.selection

import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.teacher.Teacher
import kotlinx.coroutines.flow.MutableStateFlow

interface SelectionViewModel {

    val teachers: MutableStateFlow<UiStateObject<BaseResponse<List<Teacher>>>>

    fun getAllTeachers()

    fun getTeacherGroups(teacherId: String, block: (Result<BaseResponse<List<Group>>>) -> Unit)

    fun joinToGroup(classId: String, block: (Result<BaseResponse<Any>>) -> Unit)

}