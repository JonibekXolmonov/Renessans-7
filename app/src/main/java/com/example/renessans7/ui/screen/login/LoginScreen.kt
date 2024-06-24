package com.example.renessans7.ui.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.renessans7.R
import com.example.renessans7.databinding.LoginScreenBinding
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.utils.*
import com.example.renessans7.utils.Constants.PUPIL
import com.example.renessans7.utils.Constants.TEACHER
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.login_screen) {

    private var _binding: LoginScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImp>()

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.edtUsername.addTextChangedListener {
            if (it!!.length !in 5..20) {
                binding.edtUsernameLayout.enableError(getString(R.string.str_error_username_length))
                binding.btnLogin.isClickable = false
            } else {
                binding.edtUsernameLayout.isErrorEnabled = false
                binding.btnLogin.isClickable =
                    !binding.edtPassword.isErrorOrBlank(binding.edtPasswordLayout)
            }
        }

        binding.edtPassword.addTextChangedListener {
            if (it!!.length < 6) {
                binding.edtPasswordLayout.enableError(getString(R.string.str_error_password_length))
                binding.btnLogin.isClickable = false
            } else {
                binding.edtPasswordLayout.isErrorEnabled = false
                binding.btnLogin.isClickable =
                    !binding.edtUsername.isErrorOrBlank(binding.edtUsernameLayout)
            }
        }

        binding.btnLogin.setOnClickListener {
            if (!binding.edtUsername.isNotBlank()) {
                binding.edtUsernameLayout.enableError(getString(R.string.str_error_username_length))
                return@setOnClickListener
            }
            if (!binding.edtPassword.isNotBlank()) {
                binding.edtPasswordLayout.enableError(getString(R.string.str_error_password_length))
                return@setOnClickListener
            }

            login(getUser())
        }

        binding.tvRegister.setOnClickListener {
            openRegister()
        }
    }

    private fun openRegister() {
        findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
    }

    private fun login(user: LoginRequest) {
        binding.btnLogin.setShowProgress(true)
        viewModel.login(user) {
            it.onSuccess {
                if (it.success) {
                    sharedPref.token = it.data.token
                    binding.btnLogin.disableLoading()
                    openScreen(it.data.role)
                }
            }
            it.onFailure {
                binding.btnLogin.disableLoading()
                if (it.localizedMessage?.contains("409") == true) {
                    toast(getString(R.string.str_username_error))
                } else if (it.message?.contains("406") == true) {
                    toast(getString(R.string.str_password_error))
                } else if (it.message?.contains("403") == true) {
                    toast(getString(R.string.str_locked))
                } else {
                    toast(getString(R.string.str_network_error))
                }
            }
        }
    }

    private fun getUser() = LoginRequest(
        binding.edtPassword.text.toString().trim(), binding.edtUsername.text.toString().trim()
    )

    private fun openScreen(role: String) {
        when (role) {
            PUPIL -> findNavController().navigate(R.id.action_loginScreen_to_mainScreen)
            TEACHER -> findNavController().navigate(R.id.action_loginScreen_to_teacherMainScreen)
            else -> findNavController().navigate(R.id.action_loginScreen_to_adminScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}