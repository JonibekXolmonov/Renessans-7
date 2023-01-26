package com.example.renessans7.repository

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.module.service.ApiService
import javax.inject.Inject

class TeacherRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getTeacherGroups() = apiService.getTeacherGroups()

    suspend fun addGroup(groupRequest: AddGroupRequest) = apiService.addGroup(groupRequest)

    suspend fun editGroup(editGroupRequest: AddGroupRequest,classId: String) = apiService.editGroup(editGroupRequest,classId)

    suspend fun getGroupPupils(classId: String) = apiService.getGroupDataAndMembers(classId)

    suspend fun getRequests() = apiService.getRequests()

    suspend fun accept(requestId: String) = apiService.acceptRequest(requestId)

    suspend fun decline(requestId: String) = apiService.declineRequest(requestId)

    suspend fun removePupilFromGroup(classId: String, pupilId: String) =
        apiService.removePupilFromGroup(classId, pupilId)
}