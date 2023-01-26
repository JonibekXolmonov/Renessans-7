package com.example.renessans7.ui.screen.addgroup

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.models.group.Group

interface AddGroupViewModel {

    fun addGroup(group: AddGroupRequest, block: (Result<BaseResponse<Group>>) -> Unit)

    fun getGroupById(classId: String, block: (Result<BaseResponse<com.example.renessans7.models.pupils.Class>>) -> Unit)

    fun editGroup(
        group: AddGroupRequest,
        classId: String,
        block: (Result<BaseResponse<Any>>) -> Unit
    )

}