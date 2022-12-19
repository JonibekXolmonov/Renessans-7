package com.example.renessans7.ui.screen.main

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.test.Test
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow


interface MainViewModel {

    val joinedGroups: MutableStateFlow<UiStateObject<BaseResponse<List<Group>>>>

    fun getJoinedGroups()
}