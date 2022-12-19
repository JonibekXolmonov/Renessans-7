package com.example.renessans7.ui.screen.grouptest

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.renessans7.utils.helper.UiStateObject
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupTestAdapter
import com.example.renessans7.adapter.GroupsAdapter
import com.example.renessans7.databinding.GroupTestScreenBinding
import com.example.renessans7.models.test.TestFileUploadRequest
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.test.Test
import com.example.renessans7.ui.screen.fileupload.FileUploadOptionDialog
import com.example.renessans7.utils.Constants.GALLERY
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.Constants.NAME
import com.example.renessans7.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class GroupTestScreen : Fragment(R.layout.group_test_screen) {

    private var _binding: GroupTestScreenBinding? = null
    private val binding get() = _binding!!
    private val groupTestAdapter by lazy { GroupTestAdapter() }
    private val viewModel: GroupTestViewModel by viewModels<GroupTestViewModelImp>()

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.uploadTestFile(
                    TestFileUploadRequest(
                        classId = requireArguments().get(ID).toString(),
                        testName = "juda qiyin",
                        answers = listOf("null", "A", "B", "C", "D"),
                        numberOfQuestions = 5
                    ), convertUriMultipart(result.data?.data!!)
                ) {
                    it.onSuccess {
                        if (it.success) {

                        }
                    }
                    it.onFailure {
                        toast(getString(R.string.str_error))
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroupTests(requireArguments().get(ID).toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = GroupTestScreenBinding.inflate(inflater, container, false)
        observeGroupTests()
        observeAllTests()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun observeGroupTests() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tests.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                        }

                        is UiStateObject.SUCCESS -> {
                            refreshGroupTestAdapter(it.data.data)
                        }
                        is UiStateObject.ERROR -> {

                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun observeAllTests() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allTests.collect {
                    when (it) {
                        UiStateObject.LOADING -> {
                        }

                        is UiStateObject.SUCCESS -> {
//                            refreshGroupTestAdapter(it.data.data)
                        }
                        is UiStateObject.ERROR -> {

                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding.tvGroupName.text = requireArguments().get(NAME).toString()

        binding.uploadTest.setOnClickListener {
            showSelectOption()
        }
    }

    private fun convertUriMultipart(selectedFileUri: Uri): MultipartBody.Part {
        val ins = requireActivity().contentResolver.openInputStream(selectedFileUri)
        val file = File.createTempFile(
            "file", ".pdf", requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        val fileOutputStream = FileOutputStream(file)
        ins?.copyTo(fileOutputStream)
        ins?.close()
        fileOutputStream.close()

        val reqFile: RequestBody = file.asRequestBody("file/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, reqFile)
    }

    private fun showSelectOption() {
        FileUploadOptionDialog {
            if (it == GALLERY) getFileFromGallery()
        }.show(childFragmentManager, this::class.simpleName)
    }

    private fun getFileFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startForResult.launch(intent)
    }

    private fun refreshGroupTestAdapter(groups: List<Test>) {
        groupTestAdapter.submitList(groups)
        binding.rvGroupTests.adapter = groupTestAdapter

        groupTestAdapter.onClick = { id ->
            findNavController().navigate(
                R.id.action_groupTestScreen_to_groupTestResultScreen
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}