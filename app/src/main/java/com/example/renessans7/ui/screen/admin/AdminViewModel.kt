package com.example.renessans7.ui.screen.admin

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface AdminViewModel {

    val pupils: MutableStateFlow<UiStateObject<BaseResponse<List<Pupil>>>>

    val teachers: MutableStateFlow<UiStateObject<BaseResponse<List<Teacher>>>>

    fun getAllPupils()

    fun getTeachers()


    fun removeUser(userId: String, block: (Result<BaseResponse<Any>>) -> Unit)

}