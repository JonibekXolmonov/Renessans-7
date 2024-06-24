package com.example.renessans7.ui.screen.addgroup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.renessans7.R
import com.example.renessans7.databinding.LayoutAddGroupBinding
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.pupils.Class
import com.example.renessans7.utils.Constants.BLANK
import com.example.renessans7.utils.ProgressBarDialog
import com.example.renessans7.utils.isNotEmpty
import com.example.renessans7.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupDialog(private val groupId: String, private val onAddSuccess: () -> Unit) :
    BottomSheetDialogFragment(R.layout.layout_add_group) {

    private var _binding: LayoutAddGroupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddGroupViewModel by viewModels<AddGroupViewModelImp>()
    private lateinit var loading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loading = ProgressBarDialog(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun getGroupData() {
        loading.show()
        toast(getString(R.string.str_get_group_data))
        viewModel.getGroupById(classId = groupId) {
            it.onSuccess {
                insertDataToEdittext(it.data)
                loading.dismiss()
            }
            it.onFailure {
                toast(getString(R.string.str_error_fetch_data))
                loading.dismiss()
            }
        }
    }

    private fun insertDataToEdittext(data: Class) {
        binding.edtGroupName.setText(data.name)
        binding.edtGroupDescription.setText(data.description)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LayoutAddGroupBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (groupId != BLANK)
            getGroupData()
        initViews()
    }

    private fun initViews() {
        if (groupId != BLANK) {
            binding.btnAdd.setText(R.string.str_update)
            binding.tvTitle.setText(R.string.str_group_edit)
        }

        binding.btnAdd.setOnClickListener {
            if (binding.edtGroupName.isNotEmpty() && binding.edtGroupDescription.isNotEmpty()) {
                if (groupId == BLANK)
                    addGroup()
                else
                    editGroup()
            } else {
                toast(getString(R.string.str_field_empty_group))
            }
        }

        binding.btnCancel.setOnClickListener {
            dismissNow()
        }
    }

    private fun editGroup() {
        loading.show()
        viewModel.editGroup(
            group = getGroup(),
            classId = groupId
        ) {
            it.onSuccess {
                toast(getString(R.string.str_editted_success).plus("\n").plus(it.data.toString()))
                loading.hide()
                onAddSuccess.invoke()
                dismissNow()
            }
            it.onFailure {
                toast(getString(R.string.str_error))
                loading.hide()
                dismissNow()
            }
        }
    }

    private fun addGroup() {
        loading.show()
        viewModel.addGroup(getGroup()) {
            it.onSuccess {
                onAddSuccess()
                loading.dismiss()
                dismissNow()
            }
            it.onFailure {
                loading.dismiss()
                toast(getString(R.string.str_error))
            }
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