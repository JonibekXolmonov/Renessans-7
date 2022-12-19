package com.example.renessans7.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.test.Test
import com.example.renessans7.repository.PupilRepository
import com.example.renessans7.repository.TestRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModelImp @Inject constructor(
    private val pupilRepository: PupilRepository,
    private val testRepository: TestRepository
) :
    ViewModel(), MainViewModel {

    private val _joinedGroups =
        MutableStateFlow<UiStateObject<BaseResponse<List<Group>>>>(UiStateObject.EMPTY)
    override val joinedGroups = _joinedGroups


    override fun getJoinedGroups() {
        _joinedGroups.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _joinedGroups.value = UiStateObject.SUCCESS(pupilRepository.getJoinedGroups())
            } catch (e: Exception) {
                _joinedGroups.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }
}