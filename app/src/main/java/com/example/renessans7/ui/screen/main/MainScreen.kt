package com.example.renessans7.ui.screen.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupsAdapter
import com.example.renessans7.databinding.MainScreenBinding
import com.example.renessans7.databinding.TeacherChooseScreenBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.Constants.NAME
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainScreen : Fragment(R.layout.main_screen) {

    private var _binding: MainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels<MainViewModelImp>()
    private val adapter by lazy { GroupsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getJoinedGroups()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MainScreenBinding.inflate(inflater, container, false)
        observeJoinedGroups()
        return binding.root
    }

    private fun observeJoinedGroups() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.joinedGroups.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                            binding.refreshLayout.enableRefresh()
                        }

                        is UiStateObject.SUCCESS -> {
                            binding.refreshLayout.disableRefresh()
                            if (it.data.data.isNotEmpty()) {
                                refreshAdapter(it.data.data)
                                binding.tvEmpty.hide()
                            }
                            else binding.tvEmpty.show()
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

    private fun refreshAdapter(data: List<Group>) {
        adapter.submitList(data)
        binding.rvMyGroups.adapter = adapter
        adapter.onClick = { id, name ->
            findNavController().navigate(
                R.id.action_mainScreen_to_joinedGroupTestScreen, bundleOf(
                    ID to id, NAME to name
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.ivRequestToGroup.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_teacherChooseScreen)
        }
        binding.ivCheckRequests.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_pupilNotificationScreen)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getJoinedGroups()
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