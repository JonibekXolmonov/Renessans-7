package com.example.renessans7.ui.screen.joinedGroupTests

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.Test
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface JoinedGroupTestViewModel {

    val tests: MutableStateFlow<UiStateObject<BaseResponse<List<Test>>>>

    fun getGroupTests(groupId: String)

}