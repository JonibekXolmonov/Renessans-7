package com.example.renessans7.ui.screen.admin

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
import com.example.renessans7.adapter.TeachersAdminAdapter
import com.example.renessans7.databinding.AllTeachersScreenBinding
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.*
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllTeachersScreen : Fragment(R.layout.all_teachers_screen) {

    private var _binding: AllTeachersScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by viewModels<AdminViewModelImp>()
    private val teachersAdapter by lazy { TeachersAdminAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTeachers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AllTeachersScreenBinding.inflate(inflater, container, false)
        setUpTeachersObserver()
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getTeachers()
        }
    }

    private fun setUpTeachersObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teachers.collect {
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

    private fun refreshAdapter(data: List<Teacher>) {
        teachersAdapter.submitList(data)
        binding.rvTeachers.adapter = teachersAdapter

        teachersAdapter.onClick = { teacher, isActive ->
            viewModel.removeUser(teacher.teacherId) {
                it.onSuccess {
                    if (isActive)
                        toast(getString(R.string.str_restore_success))
                    else
                        toast(getString(R.string.str_removed_success))
                    viewModel.getTeachers()
                }
                it.onFailure {
                    toast(getString(R.string.str_error))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}