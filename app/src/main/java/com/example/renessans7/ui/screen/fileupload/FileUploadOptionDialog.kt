package com.example.renessans7.ui.screen.fileupload

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.renessans7.R
import com.example.renessans7.databinding.LayoutFileSelectionOptionBinding
import com.example.renessans7.utils.Constants.EXIST_FILE
import com.example.renessans7.utils.Constants.GALLERY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FileUploadOptionDialog(private val block: (Int) -> Unit) :
    BottomSheetDialogFragment(R.layout.layout_file_selection_option) {

    private var _binding: LayoutFileSelectionOptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LayoutFileSelectionOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun initViews() {
        binding.tvGallery.setOnClickListener {
            block(GALLERY)
            dismissNow()
        }

        binding.tvExistFiles.setOnClickListener {
            block(EXIST_FILE)
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