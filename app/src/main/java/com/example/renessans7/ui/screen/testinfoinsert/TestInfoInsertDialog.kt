package com.example.renessans7.ui.screen.testinfoinsert

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import com.example.renessans7.R
import com.example.renessans7.adapter.AnswerInsertionAdapter
import com.example.renessans7.databinding.TestInfoInsertDialogBinding
import com.example.renessans7.models.test.TestAnswers
import com.example.renessans7.models.test.TestFileUploadRequest
import com.example.renessans7.utils.isNotEmpty
import com.example.renessans7.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TestInfoInsertDialog(private val block: (TestFileUploadRequest) -> Unit) :
    BottomSheetDialogFragment(R.layout.test_info_insert_dialog) {

    private var _binding: TestInfoInsertDialogBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { AnswerInsertionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = TestInfoInsertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.edtTestNumber.doAfterTextChanged {
            if (it.toString().isNotBlank()) {
                refreshAdapter(it.toString().toInt())
            }
        }

        binding.btnFinish.setOnClickListener {
            if (allAnswersInserted()) {
                toast(getString(R.string.str_not_all_answer_inserted))
            } else if (binding.edtTestNumber.isNotEmpty()) {
                block(
                    TestFileUploadRequest(
                        classId = "",
                        testName = binding.edtTestName.text.toString(),
                        answers = adapter.currentList.map { it.testAnswer },
                        numberOfQuestions = adapter.currentList.size
                    )
                )
                dismissNow()
            } else {
                toast(getString(R.string.str_field_empty))
            }
        }
    }

    private fun allAnswersInserted() = adapter.currentList.any { it.testAnswer.isBlank() }

    private fun refreshAdapter(size: Int) {
        adapter.submitList(getResult(size))
        binding.rvTestAnswers.adapter = adapter
    }

    private fun getResult(size: Int) = ArrayList<TestAnswers>().apply {
        for (i in 0 until size)
            this.add(
                TestAnswers(
                    testNumber = i + 1,
                    testAnswer = ""
                )
            )
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}