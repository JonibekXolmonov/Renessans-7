package com.example.renessans7.ui.screen.pupilrequestcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.repository.PupilRepository
import com.example.renessans7.repository.TeacherRepository
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PupilNotificationViewModelImp @Inject constructor(private val pupilRepository: PupilRepository) :
    ViewModel(), PupilNotificationViewModel {

    private val _requests =
        MutableStateFlow<UiStateObject<BaseResponse<List<RequestsToJoin>>>>(UiStateObject.EMPTY)
    override val requests = _requests

    override fun getPupilPendingRequest() {
        _requests.value = UiStateObject.LOADING
        viewModelScope.launch {
            try {
                _requests.value = UiStateObject.SUCCESS(pupilRepository.getPupilPendingRequest())
            } catch (e: Exception) {
                _requests.value =
                    UiStateObject.ERROR(e.localizedMessage ?: "No internet connection!")
            }
        }
    }


    override fun cancel(requestId: String, block: (Result<BaseResponse<Any>>) -> Unit) {
        viewModelScope.launch {
            try {
                block(Result.success(pupilRepository.cancel(requestId)))
            } catch (e: Exception) {
                block(Result.failure(e))
            }
        }
    }
}