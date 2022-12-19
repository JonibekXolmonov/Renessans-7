package com.example.renessans7.repository

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.register.RegisterRequest
import com.example.renessans7.module.service.ApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)

    suspend fun registerAsPupil(registerRequest: RegisterRequest) =
        apiService.registerAsPupil(registerRequest)

    suspend fun registerAsTeacher(registerRequest: RegisterRequest) =
        apiService.registerAsTeacher(registerRequest)

    suspend fun getAllTeachers() = apiService.getAllTeachers()

    suspend fun getTeacherGroups(teacherId: String) = apiService.getTeacherGroups(teacherId)

    suspend fun joinToGroup(classId: String) = apiService.joinToGroup(classId)

}