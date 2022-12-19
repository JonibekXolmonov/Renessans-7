package com.example.renessans7.ui.screen.acceptrequest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.example.renessans7.databinding.AcceptToGroupScreenBinding
import com.example.renessans7.databinding.GroupTestScreenBinding
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AcceptToGroupScreen : Fragment(R.layout.accept_to_group_screen) {

    private var _binding: AcceptToGroupScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AcceptToGroupViewModel by viewModels<AcceptToGroupViewModelImp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        }

                        is UiStateObject.SUCCESS -> {
                            viewModel.accept(it.data.data[0].requestId) {
                                it.onSuccess {

                                }
                                it.onFailure {
                                    Log.d("TAG", "setUpRequestObserver: ${it.localizedMessage}")
                                }
                            }
                        }
                        is UiStateObject.ERROR -> {
                            Log.d("TAG", "setUpRequestObserver: ${it.message}")

                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
