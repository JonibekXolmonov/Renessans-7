package com.example.renessans7.ui.screen.joinedGroupTests

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
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupTestAdapter
import com.example.renessans7.databinding.JoinedGroupTestScreenBinding
import com.example.renessans7.models.test.Test
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinedGroupTestScreen : Fragment(R.layout.joined_group_test_screen) {

    private var _binding: JoinedGroupTestScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JoinedGroupTestViewModel by viewModels<JoinedGroupTestViewModelImp>()
    private val adapter by lazy { GroupTestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getJoinedGroupTests()
    }

    private fun getJoinedGroupTests() {
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
                            binding.refreshLayout.enableRefresh()
                        }

                        is UiStateObject.SUCCESS -> {
                            binding.refreshLayout.disableRefresh()
                            if (it.data.data.isNotEmpty()) {
                                binding.rvGroupTests.show()
                                refreshAdapter(it.data.data.reversed())
                                binding.tvEmpty.hide()
                            } else {
                                binding.rvGroupTests.hide()
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

    private fun refreshAdapter(data: List<Test>) {
        adapter.submitList(data)
        binding.rvGroupTests.adapter = adapter
        adapter.onClick = { id ->
            findNavController().navigate(
                R.id.action_joinedGroupTestScreen_to_testSolveScreen,
                bundleOf(
                    ID to id
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.tvGroupName.text = requireArguments().get(Constants.NAME).toString()

        binding.refreshLayout.setOnRefreshListener {
            getJoinedGroupTests()
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