package com.example.renessans7.ui.screen.grouptest

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.renessans7.R
import com.example.renessans7.adapter.GroupTestAdapter
import com.example.renessans7.databinding.GroupTestScreenBinding
import com.example.renessans7.models.test.Test
import com.example.renessans7.models.test.TestFileUploadRequest
import com.example.renessans7.ui.screen.testinfoinsert.TestInfoInsertDialog
import com.example.renessans7.ui.screen.alltestsofteacher.AllTests
import com.example.renessans7.ui.screen.fileupload.FileUploadOptionDialog
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.GALLERY
import com.example.renessans7.utils.Constants.GROUP_ID
import com.example.renessans7.utils.Constants.ID
import com.example.renessans7.utils.Constants.NAME
import com.example.renessans7.utils.Constants.TEST_ID
import com.example.renessans7.utils.helper.UiStateObject
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
    private var fileUrl: Uri? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                fileUrl = result.data?.data
                showTestInfoInsertDialog()
            }
        }

    private fun showTestInfoInsertDialog() {
        TestInfoInsertDialog {
            it.classId = requireArguments().get(ID).toString()
            uploadTest(it)
        }.show(childFragmentManager, this::class.simpleName)
    }

    private fun uploadTest(it: TestFileUploadRequest) {
        binding.refreshLayout.enableRefresh()
        viewModel.uploadTestFile(
            it,
            convertUriMultipart(fileUrl!!)
        ) {
            it.onSuccess {
                if (it.success) {
                    binding.refreshLayout.disableRefresh()
                    toast(getString(R.string.str_test_add_success))
                    viewModel.getGroupTests(requireArguments().get(ID).toString())
                }
            }
            it.onFailure {
                binding.refreshLayout.disableRefresh()
                toast(getString(R.string.str_error))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getGroupTests()
    }

    private fun getGroupTests() {
        viewModel.getGroupTests(requireArguments().get(ID).toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = GroupTestScreenBinding.inflate(inflater, container, false)
        observeGroupTests()
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
                            binding.refreshLayout.enableRefresh()
                        }

                        is UiStateObject.SUCCESS -> {
                            binding.refreshLayout.disableRefresh()
                            if (it.data.data.isNotEmpty()) {
                                refreshGroupTestAdapter(it.data.data)
                                binding.tvEmpty.hide()
                            } else binding.tvEmpty.show()
                        }

                        is UiStateObject.ERROR -> {
                            binding.refreshLayout.disableRefresh()
                            toast(getString(R.string.str_error))
                        }
                        else -> {}
                    }
                }
            }
        }
    }


    private fun initViews() {
        binding.tvGroupName.text = requireArguments().get(NAME).toString()

        binding.ivBack.setOnClickListener {
            back()
        }

        binding.uploadTest.setOnClickListener {
            showSelectOption()
        }

        binding.showPupils.setOnClickListener {
            findNavController().navigate(
                R.id.action_groupTestScreen_to_groupPupilsScreen,
                bundleOf(
                    ID to requireArguments().get(ID).toString()
                )
            )
        }

        binding.refreshLayout.setOnRefreshListener {
            getGroupTests()
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
            else getTestFromHistory()
        }.show(childFragmentManager, this::class.simpleName)
    }

    private fun getTestFromHistory() {
        AllTests {
            forwardTest(it)
        }.show(childFragmentManager, this::class.simpleName)
    }

    private fun forwardTest(testId: String) {
        binding.refreshLayout.enableRefresh()
        viewModel.forwardTest(testId, requireArguments().get(ID).toString()) {
            it.onSuccess {
                toast(getString(R.string.str_forward_success))
                binding.refreshLayout.disableRefresh()
                getGroupTests()
            }
            it.onFailure {
                toast(getString(R.string.str_error))
                binding.refreshLayout.disableRefresh()
            }
        }
    }

    private fun getFileFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startForResult.launch(intent)
    }

    private fun refreshGroupTestAdapter(groups: List<Test>) {
        groupTestAdapter.submitList(groups)
        binding.rvGroupTests.adapter = groupTestAdapter

        groupTestAdapter.onClick = { testId ->
            findNavController().navigate(
                R.id.action_groupTestScreen_to_groupTestResultScreen,
                bundleOf(
                    TEST_ID to testId,
                    GROUP_ID to requireArguments().get(ID).toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}