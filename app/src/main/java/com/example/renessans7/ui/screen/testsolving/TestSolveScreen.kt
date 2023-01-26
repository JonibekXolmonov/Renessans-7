package com.example.renessans7.ui.screen.testsolving

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.example.renessans7.adapter.AnswerInsertionAdapter
import com.example.renessans7.databinding.TestSolveScreenBinding
import com.example.renessans7.models.test.TestAnswers
import com.example.renessans7.models.test.TestCheckRequest
import com.example.renessans7.ui.screen.testresult.TestResultDialog
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.Constants.NULL
import com.example.renessans7.utils.PDFUtil.loadPdfToViewer
import com.example.renessans7.utils.back
import com.example.renessans7.utils.getDuration
import com.example.renessans7.utils.helper.UiStateList
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestSolveScreen : Fragment(R.layout.test_solve_screen) {

    private var _binding: TestSolveScreenBinding? = null
    private val binding get() = _binding!!
    private val answerInsertionAdapter by lazy { AnswerInsertionAdapter() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheet: View
    private val viewModel: TestViewModel by viewModels<TestViewModelImp>()
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTestById(requireArguments().get(ID).toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = TestSolveScreenBinding.inflate(inflater, container, false)
        observeGroupTests()
        observeAnswers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
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
        } catch (e: Exception) {
            //this is tablet
        }

        binding.btnFinish.setOnClickListener {
            checkTest()
        }

        binding.ivBack.setOnClickListener {
            back()
        }
    }

    private fun checkTest() {
        viewModel.check(getTestResult()) {
            it.onSuccess {
                TestResultDialog(it.data) {
                    requireActivity().onBackPressed()
                }.show(childFragmentManager, this::class.simpleName)

            }
            it.onFailure {
                toast(getString(R.string.str_error))
            }
        }
    }

    private fun startTimeCounter(timeInMilliseconds: Long) {
        countDownTimer = object : CountDownTimer(timeInMilliseconds, 1000) {
            override fun onTick(p0: Long) {
                binding.tvTimeCounter.text = (p0 / 1000).getDuration()
            }

            override fun onFinish() {
                binding.tvTimeCounter.text = getString(R.string.str_00_00)
                checkTest()
            }
        }.start()
    }

    private fun getTestResult() = TestCheckRequest(
        testId = requireArguments().get(ID).toString(),
        pupilAnswers = answerInsertionAdapter.currentList.map { it.testAnswer }
    )

    private fun observeGroupTests() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.test.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                        }

                        is UiStateObject.SUCCESS -> {
                            loadPdfToViewer(
                                it.data.data.fileUrl,
                                binding.pdfViewer!!
                            )
                            refreshAnswerInsertion(initArray(it.data.data.numberOfQuestions))
                            startTimeCounter(getTimeInMillis(it.data.data.numberOfQuestions))
                        }
                        is UiStateObject.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun getTimeInMillis(numberOfQuestions: Int): Long = when (numberOfQuestions) {
        in 0..1 -> 30 * 1000
        in 2..9 -> 11 * 60 * 1000
        in 10..14 -> 21 * 60 * 1000
        in 15..24 -> 31 * 60 * 1000
        in 25..34 -> 41 * 60 * 1000
        else ->            59 * 60 * 1000
    }

    private fun observeAnswers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.answers.collect {
                    when (it) {
                        UiStateList.LOADING -> {
                        }

                        is UiStateList.SUCCESS -> {
                            refreshAnswerInsertion(it.data.map { an ->
                                TestAnswers(0, an)
                            })
                        }
                        is UiStateList.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initArray(numberOfQuestions: Int) = ArrayList<TestAnswers>().apply {
        for (i in 0 until numberOfQuestions)
            this.add(
                TestAnswers(i, NULL)
            )
    }

    private fun refreshAnswerInsertion(testAnswers: List<TestAnswers>) {
        answerInsertionAdapter.submitList(testAnswers)
        try {
            binding.rvTestResultInsert!!.adapter = answerInsertionAdapter
        } catch (e: Exception) {
            binding.layoutAnswers!!.rvTestResultInsert.adapter = answerInsertionAdapter
        }
    }

    override fun onPause() {
        viewModel.saveAnswers(answerInsertionAdapter.currentList.map { it.testAnswer })
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}