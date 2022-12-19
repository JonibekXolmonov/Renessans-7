package com.example.renessans7.ui.screen.testsolving

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.renessans7.R
import com.example.renessans7.adapter.AnswerInsertionAdapter
import com.example.renessans7.databinding.TestSolveScreenBinding
import com.example.renessans7.models.test.TestAnswers
import com.example.renessans7.utils.Constants.A
import com.example.renessans7.utils.PDFUtil.loadPdfToViewer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class TestSolveScreen : Fragment(R.layout.test_solve_screen) {

    private var _binding: TestSolveScreenBinding? = null
    private val binding get() = _binding!!
    private val answerInsertionAdapter by lazy { AnswerInsertionAdapter() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheet: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = TestSolveScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        loadPdfToViewer(
            "http://10.10.3.13:8082/file/8fc39307-00a5-4382-8c59-4080769b6671.pdf",
            binding.pdfViewer!!
        )

        refreshAnswerInsertion(getFake())

        try {
            bottomSheet = binding.layoutAnswers!!.root

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            binding.btnUpDown!!.setOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }catch (e:Exception){
            //this is tablet
        }
    }

    private fun getFake() = ArrayList<TestAnswers>().apply {
        for (i in 0 until Random.nextInt(20, 40))
            this.add(
                TestAnswers(i, A)
            )
    }

    private fun refreshAnswerInsertion(testAnswers: List<TestAnswers>) {
        answerInsertionAdapter.submitList(testAnswers)
        try {
            binding.rvTestResultInsert!!.adapter = answerInsertionAdapter
        } catch (e: Exception) {
            binding.layoutAnswers!!.rvTestResultInsert.adapter = answerInsertionAdapter
        }
        answerInsertionAdapter.onAnswerSelected = {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}