package com.example.renessans7.ui.screen.joinedGroupTests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupTestAdapter
import com.example.renessans7.databinding.JoinedGroupTestScreenBinding
import com.example.renessans7.databinding.MainScreenBinding
import com.example.renessans7.models.test.Test
import com.example.renessans7.ui.screen.main.MainViewModel
import com.example.renessans7.ui.screen.main.MainViewModelImp
import com.example.renessans7.utils.Constants
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.helper.UiStateObject
import kotlinx.coroutines.launch


class JoinedGroupTestScreen : Fragment(R.layout.joined_group_test_screen) {

    private var _binding: JoinedGroupTestScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JoinedGroupTestViewModel by viewModels<JoinedGroupTestViewModelImp>()
    private val adapter by lazy { GroupTestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroupTests(requireArguments().get(ID).toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = JoinedGroupTestScreenBinding.inflate(inflater, container, false)
        setUpObserver()
        return binding.root
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tests.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                        }

                        is UiStateObject.SUCCESS -> {
                            refreshAdapter(it.data.data)
                        }
                        is UiStateObject.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun refreshAdapter(data: List<Test>) {
        adapter.submitList(data)
        binding.rvGroupTests.adapter = adapter
        adapter.onClick = {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.tvGroupName.text = requireArguments().get(Constants.NAME).toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}