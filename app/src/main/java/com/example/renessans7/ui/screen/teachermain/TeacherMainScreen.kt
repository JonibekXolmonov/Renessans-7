package com.example.renessans7.ui.screen.teachermain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupsAdapter
import com.example.renessans7.databinding.TeacherMainScreenBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.ui.screen.addgroup.AddGroupDialog
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.Constants.NAME
import com.example.renessans7.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherMainScreen : Fragment(R.layout.teacher_main_screen) {

    private var _binding: TeacherMainScreenBinding? = null
    private val binding get() = _binding!!
    private val groupsAdapter by lazy { GroupsAdapter() }
    private val viewModel: TeacherGroupViewModel by viewModels<TeacherGroupViewModelImp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTeacherGroups()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = TeacherMainScreenBinding.inflate(inflater, container, false)
        observeTeacherGroups()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun observeTeacherGroups() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groups.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                        }

                        is UiStateObject.SUCCESS -> {
                            refreshGroupsAdapter(it.data.data)
                        }
                        is UiStateObject.ERROR -> {

                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initViews() {

        binding.ivAddGroup.setOnClickListener {
            AddGroupDialog {
                viewModel.getTeacherGroups()
            }.show(childFragmentManager, this::class.simpleName)
        }

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(R.id.action_teacherMainScreen_to_acceptToGroupScreen)
        }
    }


    private fun refreshGroupsAdapter(groups: List<Group>) {
        groupsAdapter.submitList(groups)
        groupsAdapter.setPosToDefault()
        binding.rvMyGroups.adapter = groupsAdapter

        groupsAdapter.onClick = { id, name ->
            findNavController().navigate(
                R.id.action_teacherMainScreen_to_groupTestScreen, bundleOf(
                    ID to id, NAME to name
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}