package com.example.renessans7.ui.screen.addgroup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.renessans7.R
import com.example.renessans7.databinding.LayoutAddGroupBinding
import com.example.renessans7.databinding.LayoutFileSelectionOptionBinding
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.models.group.Group
import com.example.renessans7.utils.Constants.EXIST_FILE
import com.example.renessans7.utils.Constants.GALLERY
import com.example.renessans7.utils.ProgressBarDialog
import com.example.renessans7.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupDialog(private val onAddSuccess: (Group) -> Unit) :
    BottomSheetDialogFragment(R.layout.layout_add_group) {

    private var _binding: LayoutAddGroupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddGroupViewModel by viewModels<AddGroupViewModelImp>()
    private lateinit var loading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = ProgressBarDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LayoutAddGroupBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.btnAdd.setOnClickListener {
            loading.show()
            viewModel.addGroup(getGroup()) {
                it.onSuccess {
                    onAddSuccess(it.data)
                    loading.dismiss()
                    dismissNow()
                }
                it.onFailure {
                    loading.dismiss()
                    toast(getString(R.string.str_error))
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            dismissNow()
        }
    }

    private fun getGroup() = AddGroupRequest(
        binding.edtGroupName.text.toString().trim(),
        binding.edtGroupDescription.text.toString().trim()
    )

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}