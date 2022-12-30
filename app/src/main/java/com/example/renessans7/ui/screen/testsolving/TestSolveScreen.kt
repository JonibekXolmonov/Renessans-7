package com.example.renessans7.ui.screen.testsolving

import android.os.Bundle
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
import com.example.renessans7.utils.Constants.A
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.Constants.NULL
import com.example.renessans7.utils.Constants.SPACE
import com.example.renessans7.utils.Constants.TEST_BASE_URL
import com.example.renessans7.utils.PDFUtil.loadPdfToViewer
import com.example.renessans7.utils.back
import com.example.renessans7.utils.helper.UiStateList
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class TestSolveScreen : Fragment(R.layout.test_solve_screen) {

    private var _binding: TestSolveScreenBinding? = null
    private val binding get() = _binding!!
    private val answerInsertionAdapter by lazy { AnswerInsertionAdapter() }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheet: View
    private val viewModel: TestViewModel by viewModels<TestViewModelImp>()

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

        binding.ivBack.setOnClickListener {
            back()
        }
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
                                TEST_BASE_URL.plus(it.data.data.fileUrl),
                                binding.pdfViewer!!
                            )
                            refreshAnswerInsertion(initArray(it.data.data.numberOfQuestions))
                        }
                        is UiStateObject.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
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
                            Log.d("TAG", "observeAnswers: ${it.data}")
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
}