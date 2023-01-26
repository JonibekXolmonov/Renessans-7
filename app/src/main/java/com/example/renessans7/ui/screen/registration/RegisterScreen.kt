package com.example.renessans7.ui.screen.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.renessans7.R
import com.example.renessans7.databinding.RegisterScreenBinding
import com.example.renessans7.models.register.RegisterRequest
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.SPACE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.register_screen) {

    private var _binding: RegisterScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImp>()

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RegisterScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {


        binding.btnRegister.isClickable = false

        binding.edtUsername.addTextChangedListener {
            if (it!!.length !in 5..20) {
                binding.edtUsernameLayout.enableError(getString(R.string.str_error_username_length))
                binding.btnRegister.isClickable = false
            } else {
                binding.edtUsernameLayout.isErrorEnabled = false
                binding.btnRegister.isClickable =
                    binding.edtPassword.isNotBlank() || binding.edtRePassword.isNotBlank() || binding.edtFullName.isNotBlank()
            }
        }

        binding.edtPassword.addTextChangedListener {
            if (it!!.length < 6) {
                binding.edtPasswordLayout.enableError(getString(R.string.str_error_password_length))
                binding.btnRegister.isClickable = false
            } else {
                binding.edtPasswordLayout.isErrorEnabled = false
                binding.btnRegister.isClickable =
                    binding.edtRePassword.isNotBlank() || binding.edtFullName.isNotBlank() || binding.edtUsername.isNotBlank()
            }
        }

        binding.edtRePassword.addTextChangedListener {
            if (isRetypedCorrectly()) binding.edtRePasswordLayout.isErrorEnabled = false
        }

        binding.btnRegister.setOnClickListener {
            if (!isRetypedCorrectly()) binding.edtRePasswordLayout.enableError(getString(R.string.str_retype_incorrrect))
            else if (!isFullNameCorrectInput()) toast(getString(R.string.str_fullname_not_filled))
            else if (!binding.edtUsername.isNotBlank()) binding.edtUsernameLayout.enableError(
                getString(R.string.str_error_username_length)
            )
            else register()
        }

    }

    private fun register() {
        binding.btnRegister.setLoading(requireContext())
        registerPupil()
    }

    private fun registerPupil() {
        viewModel.registerAsPupil(getUser()) {
            it.onSuccess {
                binding.btnRegister.setLoading(requireContext())
                sharedPref.token = it.data.token
                findNavController().navigate(R.id.action_registerScreen_to_mainScreen)
            }
            it.onFailure {
                binding.btnRegister.disableLoading()
                if (it.localizedMessage?.contains("409") == true) {
                    toast(getString(R.string.str_username_error))
                } else if (it.message?.contains("406") == true) {
                    toast(getString(R.string.str_password_error))
                } else {
                    toast(getString(R.string.str_network_error))
                }
            }
        }
    }

    private fun registerTeacher() {
        viewModel.registerAsTeacher(getUser()) {
            it.onSuccess {
                binding.btnRegister.disableLoading()
                sharedPref.token = it.data.token
                findNavController().navigate(R.id.action_registerScreen_to_teacherMainScreen)
            }
            it.onFailure {
                binding.btnRegister.disableLoading()
                if (it.localizedMessage?.contains("409") == true) {
                    toast(getString(R.string.str_username_error))
                } else if (it.message?.contains("406") == true) {
                    toast(getString(R.string.str_password_error))
                } else {
                    toast(getString(R.string.str_network_error))
                }
            }
        }
    }

    private fun getUser() = RegisterRequest(
        binding.edtFullName.text.toString().trim(),
        password = binding.edtPassword.text.toString().trim(),
        rePassword = binding.edtRePassword.text.toString().trim(),
        username = binding.edtUsername.text.toString().trim()
    )

    private fun isFullNameCorrectInput() = binding.edtFullName.text.toString().split(SPACE).size > 2

    private fun isRetypedCorrectly() =
        (binding.edtPassword.text.toString() == binding.edtRePassword.text.toString()) && binding.edtPassword.isNotBlank()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}