package com.example.renessans7.ui.screen.addgroup

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.models.group.Group

interface AddGroupViewModel {

    fun addGroup(group: AddGroupRequest, block: (Result<BaseResponse<Group>>) -> Unit)

}