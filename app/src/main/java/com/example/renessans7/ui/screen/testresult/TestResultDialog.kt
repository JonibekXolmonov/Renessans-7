package com.example.renessans7.ui.screen.testresult

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.renessans7.R
import com.example.renessans7.databinding.LayoutTestResultBinding
import com.example.renessans7.models.test.TestCheckResponse

class TestResultDialog(
    private val testResult: TestCheckResponse, private val onNextClick: () -> Unit
) : DialogFragment(R.layout.layout_test_result) {

    private lateinit var binding: LayoutTestResultBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LayoutTestResultBinding.bind(view)
        initViews()
    }

    private fun initViews() {
        binding.apply {

            tvCorrectAnswersScore.text = testResult.trueAnswers.toString()
            tvIncorrectAnswersScore.text = testResult.wrongAnswers.toString()
            tvMissedScore.text = testResult.unanswered.toString()

            cvNext.setOnClickListener {
                onNextClick.invoke()
                dismissNow()
            }
        }
        isCancelable = false
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}