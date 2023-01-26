package com.example.renessans7.ui.screen.grouptest

import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.TestFileUploadRequest
import com.example.renessans7.models.test.Test
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MultipartBody

interface GroupTestViewModel {

    val tests: MutableStateFlow<UiStateObject<BaseResponse<List<Test>>>>
    val allTests: MutableStateFlow<UiStateObject<BaseResponse<List<Test>>>>

    fun getGroupTests(groupId: String)

    fun getAllTests()

    fun uploadTestFile(
        testFileUploadRequest: TestFileUploadRequest,
        file: MultipartBody.Part,
        block: (Result<BaseResponse<Test>>) -> Unit
    )

    fun forwardTest(testId: String, classId: String, block: (Result<BaseResponse<Any>>) -> Unit)

    fun updateTestVisibility(
        testId: String,
        classId: String,
        visible: Boolean,
        block: (Result<BaseResponse<Any>>) -> Unit
    )

}