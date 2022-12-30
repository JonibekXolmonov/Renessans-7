package com.example.renessans7.ui.screen.alltestsofteacher

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupTestAdapter
import com.example.renessans7.databinding.AllTestDialogBinding
import com.example.renessans7.models.test.Test
import com.example.renessans7.ui.screen.grouptest.GroupTestViewModel
import com.example.renessans7.ui.screen.grouptest.GroupTestViewModelImp
import com.example.renessans7.utils.ProgressBarDialog
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllTests(private val block: (String) -> Unit) :
    BottomSheetDialogFragment(R.layout.all_test_dialog) {

    private var _binding: AllTestDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupTestViewModel by viewModels<GroupTestViewModelImp>()
    private val adapter by lazy { GroupTestAdapter() }
    private lateinit var loading: ProgressBarDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = ProgressBarDialog(requireContext())
        viewModel.getAllTests()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AllTestDialogBinding.inflate(inflater, container, false)
        observeAllTests()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private fun observeAllTests() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allTests.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                            loading.show()
                        }

                        is UiStateObject.SUCCESS -> {
                            refreshGroupTestAdapter(it.data.data)
                            loading.dismiss()
                        }
                        is UiStateObject.ERROR -> {
                            loading.dismiss()
                            toast(getString(R.string.str_error))
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun refreshGroupTestAdapter(data: List<Test>) {
        adapter.submitList(data)
        binding.rvAllTests.adapter = adapter

        adapter.onClick = {
            block(it)
            dismissNow()
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}