package com.troia.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.troia.app.databinding.ActivityLoginBinding
import com.troia.app.fragment.DialogCadastro
import com.troia.app.viewmodel.LoginViewModel
import com.troia.core.R
import com.troia.core.database.DataNotifier
import com.troia.core.fragment.GeneralDialog
import com.troia.core.types.User
import com.troia.core.utils.PreferencesManager
import com.troia.core.utils.UserUtils

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var userEmail = ""
    private var userRawPass = ""
    private var userPass = ""
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        DataNotifier.subscribe(viewModel)
        val (email, pass) = PreferencesManager.getLogin()
        if (email != null && pass != null) {
            userEmail = email
            userPass = pass
            viewModel.getUserData(email)
        }
    }

    override fun onStop() {
        super.onStop()
        DataNotifier.unsubscribe(viewModel)
    }

    fun setupListeners() {
        binding.register.setOnClickListener {
            val dialog = DialogCadastro()
            dialog.show(supportFragmentManager, DialogCadastro.TAG)
        }

        binding.buttonLogin.setOnClickListener {
            userEmail = binding.editEmail.text.toString().trim()
            userRawPass = binding.editPass.text.toString().trim()
            userPass = viewModel.encrypt(userRawPass)
            validadeLogin()
        }

        viewModel.userData.observe(this) {
            if(it != null) {
                val name = it.first
                val pass = it.second
                val admin = it.third == "1"
                if(pass == userPass){
                    UserUtils.setUser(
                        User(name, userEmail, admin)
                    )
                    PreferencesManager.setLogin(userEmail, userPass)
                    val myIntent = Intent(this, CaixinhaActivity::class.java)
                    startActivity(myIntent)
                    finish()
                } else {
                    showDialog()
                }
            }
        }
    }

    fun validadeLogin() {
        if (userEmail == "" || userRawPass == "") {
            showDialog()
            return
        }
        viewModel.getUserData(userEmail)
    }

    fun showDialog() {
        val alert = GeneralDialog.newInstance(
            getString(R.string.login),
            getString(R.string.wrong_user),
            getString(R.string.labelOk),
            {

            }
        )
        alert.show(supportFragmentManager, GeneralDialog.TAG)
    }

}