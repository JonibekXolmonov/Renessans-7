package com.example.renessans7.ui.screen.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.renessans7.R
import com.example.renessans7.adapter.TableViewPagerAdapter
import com.example.renessans7.databinding.AdminScreenBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminScreen : Fragment(R.layout.admin_screen) {

    private var _binding: AdminScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var tableViewPagerAdapter: TableViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableViewPagerAdapter = TableViewPagerAdapter(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AdminScreenBinding.inflate(inflater, container, false)
        controlViewPagerState()
        return binding.root
    }

    private fun controlViewPagerState() {
        binding.apply {
            addFragmentsToVPUser()
            vpAdmin.adapter = tableViewPagerAdapter
            val labels = listOf("Pupils", "Teachers")

            TabLayoutMediator(tabLayoutPitch, vpAdmin) { tab, position ->
                tab.text = labels[position]
            }.attach()
        }
    }

    private fun addFragmentsToVPUser() {
        tableViewPagerAdapter.addFragment(AllPupilsScreen())
        tableViewPagerAdapter.addFragment(AllTeachersScreen())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}