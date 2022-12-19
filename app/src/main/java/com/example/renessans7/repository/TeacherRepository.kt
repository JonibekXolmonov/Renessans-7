package com.example.renessans7.repository

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.module.service.ApiService
import javax.inject.Inject

class TeacherRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getTeacherGroups() = apiService.getTeacherGroups()

    suspend fun addGroup(groupRequest: AddGroupRequest) = apiService.addGroup(groupRequest)

    suspend fun getRequests() = apiService.getRequests()

    suspend fun accept(requestId: String) = apiService.acceptRequest(requestId)

    suspend fun decline(requestId: String) = apiService.declineRequest(requestId)

}