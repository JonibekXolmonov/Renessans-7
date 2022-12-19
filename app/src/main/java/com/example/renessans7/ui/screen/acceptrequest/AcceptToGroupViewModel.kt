package com.example.renessans7.ui.screen.acceptrequest

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface AcceptToGroupViewModel {

    val requests: MutableStateFlow<UiStateObject<BaseResponse<List<RequestsToJoin>>>>

    fun getRequests()

    fun accept(requestId: String, block: (Result<BaseResponse<Any>>) -> Unit)

    fun decline(requestId: String, block: (Result<BaseResponse<Any>>) -> Unit)

}