package com.example.renessans7.ui.screen.selection

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
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupsAdapter
import com.example.renessans7.adapter.TeachersAdapter
import com.example.renessans7.databinding.TeacherChooseScreenBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.Constants.SPACE
import com.example.renessans7.utils.hide
import com.example.renessans7.utils.show
import com.example.renessans7.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherChooseScreen : Fragment(R.layout.teacher_choose_screen) {

    private var _binding: TeacherChooseScreenBinding? = null
    private val binding get() = _binding!!
    private val teachersAdapter by lazy { TeachersAdapter() }
    private val groupsAdapter by lazy { GroupsAdapter() }
    private val viewModel: SelectionViewModel by viewModels<SelectionViewModelImp>()
    private var classId = SPACE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllTeachers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = TeacherChooseScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTeachers()
        initViews()
    }

    private fun observeTeachers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teachers.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                        }

                        is UiStateObject.SUCCESS -> {
                            refreshTeachersAdapter(it.data.data)
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
        binding.btnJoin.setOnClickListener {
            viewModel.joinToGroup(classId) {
                it.onSuccess {
                    toast(getString(R.string.str_request_sent))
                }
                it.onFailure {
                }
            }
        }
    }

    private fun refreshTeachersAdapter(teachers: List<Teacher>) {
        teachersAdapter.submitList(teachers)
        binding.rvTeachers.adapter = teachersAdapter

        teachersAdapter.onClick = {
            getGroups(it.teacherId)
            binding.tvTeacherName.text = it.fullName.plus(getString(R.string.str_teacher_group))
            binding.btnJoin.hide()
        }
    }

    private fun getGroups(teacherId: String) {
        viewModel.getTeacherGroups(teacherId) {
            it.onSuccess {
                refreshGroupsAdapter(it.data)
            }
            it.onFailure {
            }
        }
    }

    private fun refreshGroupsAdapter(groups: List<Group>) {
        groupsAdapter.submitList(groups)
        groupsAdapter.setPosToDefault()
        binding.rvTeacherGroups.adapter = groupsAdapter

        groupsAdapter.onClick = { id, _ ->
            classId = id
            binding.btnJoin.show()
            binding.btnJoin.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}