package com.example.renessans7.ui.screen.pupilrequestcheck

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

interface PupilNotificationViewModel {

    val requests: MutableStateFlow<UiStateObject<BaseResponse<List<RequestsToJoin>>>>

    fun getPupilPendingRequest()

    fun cancel(requestId: String, block: (Result<BaseResponse<Any>>) -> Unit)

}