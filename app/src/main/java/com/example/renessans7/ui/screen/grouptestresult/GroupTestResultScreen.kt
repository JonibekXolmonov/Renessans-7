package com.example.renessans7.ui.screen.grouptestresult

import android.app.Dialog
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
import com.example.renessans7.adapter.GroupTestResultAdapter
import com.example.renessans7.adapter.TestNumberAdapter
import com.example.renessans7.databinding.GroupTestResultScreenBinding
import com.example.renessans7.models.test.TestCheckResponse
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.GROUP_ID
import com.example.renessans7.utils.Constants.TEST_ID
import com.example.renessans7.utils.helper.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupTestResultScreen : Fragment(R.layout.group_test_result_screen) {

    private var _binding: GroupTestResultScreenBinding? = null
    private val binding get() = _binding!!
    private val groupTestResultAdapter by lazy { GroupTestResultAdapter() }
    private val testNumberAdapter by lazy { TestNumberAdapter() }
    private val viewModel: GroupTestResultViewModel by viewModels<GroupTestResultViewModelImp>()
    private lateinit var loading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroupTestResult(
            requireArguments().get(TEST_ID).toString(), requireArguments().get(GROUP_ID).toString()
        )
        loading = ProgressBarDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = GroupTestResultScreenBinding.inflate(inflater, container, false)
        initResultObserver()
        return binding.root
    }

    private fun initResultObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.results.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                            loading.show()
                        }

                        is UiStateObject.SUCCESS -> {
                            loading.dismiss()
                            if (it.data.data.isNotEmpty()) {
                                refreshTestResultAdapter(it.data.data)
                                binding.tvEmpty.hide()
                            } else binding.tvEmpty.show()
                        }
                        is UiStateObject.ERROR -> {
                            toast(getString(R.string.str_error))
                            loading.dismiss()
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
        binding.rvGroupTestResults.isNestedScrollingEnabled = false

        binding.ivBack.setOnClickListener {
            back()
        }
    }

    private fun refreshTestResultAdapter(results: ArrayList<TestCheckResponse>) {
        refreshTestNumberAdapter(results[0].result.size)
        groupTestResultAdapter.submitList(results)
        binding.rvGroupTestResults.adapter = groupTestResultAdapter
    }

    private fun refreshTestNumberAdapter(size: Int) {
        binding.testNumber.rvTestNumber.isNestedScrollingEnabled = false
        testNumberAdapter.submitList(getTestNumber(size))
        binding.testNumber.rvTestNumber.adapter = testNumberAdapter
    }

    private fun getTestNumber(size: Int) = ArrayList<Int>().apply {
        for (i in 0 until size) this.add(i + 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}