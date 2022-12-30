package com.example.renessans7.ui.screen.grouptestresult

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.TestCheckResponse
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface GroupTestResultViewModel {

    val results: MutableStateFlow<UiStateObject<BaseResponse<ArrayList<TestCheckResponse>>>>

    fun getGroupTestResult(testId: String, classId: String)

}