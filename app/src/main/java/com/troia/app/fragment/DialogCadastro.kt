package com.troia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.troia.core.R
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.troia.app.databinding.RegisterFragmentBinding
import com.troia.app.viewmodel.LoginViewModel
import com.troia.core.fragment.GeneralDialog

class DialogCadastro: DialogFragment() {

    companion object {
        const val TAG = "DialogCadastro"
    }

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var viewModel: LoginViewModel
    private var userEmail = ""
    private var userPass = ""
    private var userName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.errorAlert.visibility = View.GONE
        setupListeners()
    }

    fun setupListeners() {
        binding.buttonRegister.setOnClickListener {
            binding.errorAlert.visibility = View.GONE
            userEmail = binding.editEmail.text.toString().trim()
            userName = binding.editName.text.toString().trim()
            userPass = binding.editPass.text.toString().trim()
            val confirmPass = binding.editPassConfirm.text.toString().trim()

            if(validateFields(userName, userEmail, userPass) && validatePass(userPass, confirmPass)) {
                viewModel.validateEmail(userEmail)
            }
        }

        viewModel.userValidateResult.observe(viewLifecycleOwner) {
            if(it != null) {
                if(it.first) {
                    register(userEmail, userName, userPass)
                } else {
                    binding.errorAlert.visibility = View.VISIBLE
                    binding.errorAlert.text = it.second
                }
            }
        }
    }

    fun register(email:String, name:String, pass:String) {
        val success = viewModel.register(name, email, pass)
        if(success) {
            val alert = GeneralDialog.newInstance(
                getString(R.string.register_confirm),
                getString(R.string.register_success),
                getString(R.string.labelOk),
                {
                    this@DialogCadastro.dismiss()
                    viewModel.userValidateResult.value = null
                }
            )
            alert.show(childFragmentManager, GeneralDialog.TAG)
        }
    }

    fun validateFields(name:String, email:String, pass:String): Boolean {
        if(name == "" || email == "" || pass == "") {
            binding.errorAlert.visibility = View.VISIBLE
            binding.errorAlert.text = getString(R.string.empty_field)
            return false
        }
        return true
    }

    fun validatePass(pass: String, confirm:String): Boolean {
        if(pass != confirm) {
            binding.errorAlert.visibility = View.VISIBLE
            binding.errorAlert.text = getString(R.string.diferent_pass)
            return false
        }
        return true
    }
}