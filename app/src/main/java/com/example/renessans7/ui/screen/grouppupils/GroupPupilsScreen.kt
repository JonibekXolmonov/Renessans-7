package com.example.renessans7.ui.screen.grouppupils

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
import com.example.renessans7.adapter.GroupPupilAdapter
import com.example.renessans7.databinding.GroupPupilsScreenBinding
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupPupilsScreen : Fragment(R.layout.group_pupils_screen) {

    private var _binding: GroupPupilsScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupPupilsViewModel by viewModels<GroupPupilsViewModelImp>()
    private val groupPupilAdapter by lazy { GroupPupilAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPupils()
    }

    private fun getPupils() {
        viewModel.getGroupPupils(requireArguments().get(ID).toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = GroupPupilsScreenBinding.inflate(inflater, container, false)
        setUpRequestObserver()
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.refreshLayout.setOnRefreshListener {
            getPupils()
        }

        binding.ivBack.setOnClickListener {
            back()
        }
    }

    private fun setUpRequestObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pupils.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                            binding.refreshLayout.enableRefresh()
                        }

                        is UiStateObject.SUCCESS -> {
                            binding.refreshLayout.disableRefresh()
                            binding.tvGroupName.text = it.data.data.name
                            val groups = it.data.data.pupils.filter { it.isEnabled }
                            if (groups.isNotEmpty()) {
                                binding.rvGroupPupils.show()
                                refreshAdapter(groups)
                                binding.tvEmpty.hide()
                            } else {
                                binding.rvGroupPupils.hide()
                                binding.tvEmpty.show()
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

    private fun refreshAdapter(data: List<Pupil>) {
        groupPupilAdapter.submitList(data)
        binding.rvGroupPupils.adapter = groupPupilAdapter

        groupPupilAdapter.onRemove = {
            viewModel.removePupilFromGroup(requireArguments().get(ID).toString(), it) {
                it.onSuccess {
                    getPupils()
                    toast(getString(R.string.str_removed_success))
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