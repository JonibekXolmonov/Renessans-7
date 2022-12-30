package com.example.renessans7.ui.screen.grouptestresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.Test
import com.example.renessans7.models.test.TestCheckResponse
import com.example.renessans7.repository.TestRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupTestResultViewModelImp @Inject constructor(private val testRepository: TestRepository) :
    ViewModel(), GroupTestResultViewModel {

    private val _results =
        MutableStateFlow<UiStateObject<BaseResponse<ArrayList<TestCheckResponse>>>>(UiStateObject.EMPTY)
    override val results = _results


    override fun getGroupTestResult(testId: String, classId: String) {
        _results.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _results.value =
                    UiStateObject.SUCCESS(testRepository.getGroupTestResult(testId, classId))
            } catch (e: Exception) {
                _results.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }
}