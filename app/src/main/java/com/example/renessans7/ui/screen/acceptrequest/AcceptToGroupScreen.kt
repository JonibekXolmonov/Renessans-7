package com.example.renessans7.ui.screen.acceptrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.example.renessans7.adapter.SentRequestAdapter
import com.example.renessans7.databinding.AcceptToGroupScreenBinding
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.ACCEPT
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AcceptToGroupScreen : Fragment(R.layout.accept_to_group_screen) {

    private var _binding: AcceptToGroupScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AcceptToGroupViewModel by viewModels<AcceptToGroupViewModelImp>()
    private val adapter by lazy { SentRequestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRequests()
    }

    private fun getRequests() {
        viewModel.getRequests()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AcceptToGroupScreenBinding.inflate(inflater, container, false)
        setUpRequestObserver()
        return binding.root
    }

    private fun setUpRequestObserver() {
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
                                binding.rvJoinRequests.show()
                                refreshAdapter(it.data.data)
                                binding.tvEmpty.hide()
                            } else {
                                binding.tvEmpty.show()
                                binding.rvJoinRequests.hide()
                            }
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
        adapter.submitList(data)
        binding.rvJoinRequests.adapter = adapter

        adapter.onClick = { id, type ->
            if (type == ACCEPT) accept(id)
            else decline(id)
        }
    }

    private fun decline(id: String) {
        viewModel.decline(id) {
            it.onSuccess {
                getRequests()
                toast(getString(R.string.str_removed_success))
            }
            it.onFailure {
                toast(getString(R.string.str_error))
            }
        }
    }

    private fun accept(id: String) {
        viewModel.accept(id) {
            it.onSuccess {
                toast(getString(R.string.str_accept_success))
                getRequests()
            }
            it.onFailure {
                toast(getString(R.string.str_error))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.refreshLayout.setOnRefreshListener {
            getRequests()
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
