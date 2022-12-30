package com.example.renessans7.repository

import com.example.renessans7.module.service.ApiService
import javax.inject.Inject

class PupilRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getJoinedGroups() = apiService.getJoinedGroups()

    suspend fun getPupilPendingRequest() = apiService.getPupilPendingRequest()

    suspend fun cancel(requestId: String) = apiService.cancel(requestId)


}