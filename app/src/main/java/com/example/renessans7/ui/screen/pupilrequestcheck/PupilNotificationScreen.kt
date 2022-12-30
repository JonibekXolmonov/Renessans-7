package com.example.renessans7.ui.screen.pupilrequestcheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.renessans7.R
import com.example.renessans7.adapter.PupilRequestAdapter
import com.example.renessans7.databinding.MainScreenBinding
import com.example.renessans7.databinding.PupilNotificationScreenBinding
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.utils.*
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PupilNotificationScreen : Fragment(R.layout.pupil_notification_screen) {

    private var _binding: PupilNotificationScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PupilNotificationViewModel by viewModels<PupilNotificationViewModelImp>()
    private val pupilRequestAdapter by lazy { PupilRequestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPupilPendingRequest()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = PupilNotificationScreenBinding.inflate(inflater, container, false)
        observeJoinedGroups()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun observeJoinedGroups() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.requests.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                            binding.refreshLayout.enableRefresh()
                        }

                        is UiStateObject.SUCCESS -> {
                            binding.refreshLayout.disableRefresh()
                            if (it.data.data.isNotEmpty()) {
                                refreshAdapter(it.data.data)
                                binding.tvEmpty.hide()
                            } else binding.tvEmpty.show()
                        }
                        is UiStateObject.ERROR -> {
                            binding.refreshLayout.disableRefresh()
                            toast(getString(R.string.str_error))
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun refreshAdapter(data: List<RequestsToJoin>) {
        pupilRequestAdapter.submitList(data)
        binding.rvMyRequests.adapter = pupilRequestAdapter

        pupilRequestAdapter.onClick = {
            cancel(it)
        }
    }

    private fun cancel(requestId: String) {
        viewModel.cancel(requestId) {
            it.onSuccess {
                toast(getString(R.string.str_removed_success))
            }
            it.onFailure {
                toast(getString(R.string.str_error))
            }
        }
    }

    private fun initViews() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getPupilPendingRequest()
        }
        binding.ivBack.setOnClickListener {
            back()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}