package com.example.renessans7.ui.screen.grouptestresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupTestResultAdapter
import com.example.renessans7.adapter.GroupsAdapter
import com.example.renessans7.databinding.GroupTestResultScreenBinding
import com.example.renessans7.models.TestResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupTestResultScreen : Fragment(R.layout.group_test_result_screen) {

    private var _binding: GroupTestResultScreenBinding? = null
    private val binding get() = _binding!!
    private val groupTestResultAdapter by lazy { GroupTestResultAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = GroupTestResultScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        refreshTestResultAdapter(getResults())
        binding.rvGroupTestResults.isNestedScrollingEnabled = false
    }

    private fun refreshTestResultAdapter(results: java.util.ArrayList<TestResult>) {
        groupTestResultAdapter.submitList(results)
        binding.rvGroupTestResults.adapter = groupTestResultAdapter
    }

    private fun getResults() = ArrayList<TestResult>().apply {
        for (i in 0..50)
            this.add(TestResult())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}