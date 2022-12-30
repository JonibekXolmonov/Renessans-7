package com.example.renessans7.repository

import com.example.renessans7.module.service.ApiService
import javax.inject.Inject

class AdminRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllPupils() = apiService.getAllPupils()

    suspend fun getTeachers() = apiService.getAllTeachers()

    suspend fun removeUser(userId:String) = apiService.removeUser(userId)

}