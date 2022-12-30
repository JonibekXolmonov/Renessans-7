package com.example.renessans7.ui.screen.grouptest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.test.Test
import com.example.renessans7.models.test.TestFileUploadRequest
import com.example.renessans7.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GroupTestViewModelImp @Inject constructor(private val testRepository: TestRepository) :
    ViewModel(), GroupTestViewModel {

    private val _tests =
        MutableStateFlow<UiStateObject<BaseResponse<List<Test>>>>(UiStateObject.EMPTY)
    override val tests = _tests

    private val _allTests =
        MutableStateFlow<UiStateObject<BaseResponse<List<Test>>>>(UiStateObject.EMPTY)
    override val allTests = _allTests

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

    override fun getAllTests() {
        _allTests.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _allTests.value = UiStateObject.SUCCESS(testRepository.getAllTests())
            } catch (e: Exception) {
                _allTests.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }

    override fun uploadTestFile(
        testFileUploadRequest: TestFileUploadRequest,
        file: MultipartBody.Part,
        block: (Result<BaseResponse<Test>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(testRepository.uploadTestFile(testFileUploadRequest, file)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }

    override fun forwardTest(
        testId: String,
        classId: String,
        block: (Result<BaseResponse<Any>>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                block(Result.success(testRepository.forwardTest(testId, classId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}