package com.example.renessans7.ui.screen.joinedGroupTests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.Test
import com.example.renessans7.repository.TestRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinedGroupTestViewModelImp @Inject constructor(private val testRepository: TestRepository) :
    ViewModel(), JoinedGroupTestViewModel {

    private val _tests =
        MutableStateFlow<UiStateObject<BaseResponse<List<Test>>>>(UiStateObject.EMPTY)
    override val tests = _tests

    override fun getGroupTests(groupId: String) {
        _tests.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _tests.value = UiStateObject.SUCCESS(testRepository.getGroupTests(groupId))
            } catch (e: Exception) {
                _tests.value = UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }
}