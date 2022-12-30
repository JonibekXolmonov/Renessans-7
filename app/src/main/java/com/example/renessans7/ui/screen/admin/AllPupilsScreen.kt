package com.example.renessans7.ui.screen.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.example.renessans7.adapter.PupilsAdminAdapter
import com.example.renessans7.adapter.TeachersAdminAdapter
import com.example.renessans7.databinding.AllPupilsScreenBinding
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.utils.*
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllPupilsScreen : Fragment(R.layout.all_pupils_screen) {

    private var _binding: AllPupilsScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by viewModels<AdminViewModelImp>()
    private val pupilsAdapter by lazy { PupilsAdminAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllPupils()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AllPupilsScreenBinding.inflate(inflater, container, false)
        setUpPupilsObserver()
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getAllPupils()
        }
    }

    private fun setUpPupilsObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pupils.collect {
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

    private fun refreshAdapter(data: List<Pupil>) {
        pupilsAdapter.submitList(data)
        binding.rvPupils.adapter = pupilsAdapter

        pupilsAdapter.onClick = { pupil, isActive ->
            viewModel.removeUser(pupil.pupilId) {
                it.onSuccess {
                    if (isActive)
                        toast(getString(R.string.str_restore_success))
                    else
                        toast(getString(R.string.str_removed_success))
                    viewModel.getAllPupils()
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