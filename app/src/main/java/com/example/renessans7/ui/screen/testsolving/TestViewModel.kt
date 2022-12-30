package com.example.renessans7.ui.screen.testsolving

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.Test
import com.example.renessans7.models.test.TestCheckRequest
import com.example.renessans7.models.test.TestCheckResponse
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface TestViewModel {

    val test: MutableStateFlow<UiStateObject<BaseResponse<Test>>>

    fun getTestById(testId: String)

    fun check(
        testCheckRequest: TestCheckRequest,
        block: (Result<BaseResponse<TestCheckResponse>>) -> Unit
    )

}