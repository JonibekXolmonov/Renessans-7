package com.example.renessans7.repository

import com.example.renessans7.models.test.TestFileUploadRequest
import com.example.renessans7.module.service.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class TestRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getGroupTests(groupId: String) = apiService.getGroupTests(groupId)

    suspend fun getAllTests() = apiService.getAllTests()

    suspend fun uploadTestFile(
        testFileUploadRequest: TestFileUploadRequest, file: MultipartBody.Part
    ) = apiService.uploadTestFile(testFileUploadRequest, file)
}