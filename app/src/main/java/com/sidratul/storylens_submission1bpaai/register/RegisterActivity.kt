package com.sidratul.storylens_submission1bpaai.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import com.sidratul.storylens_submission1bpaai.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.sidratul.storylens_submission1bpaai.databinding.ActivityRegisterBinding
import com.sidratul.storylens_submission1bpaai.ViewModelFactory
import com.sidratul.storylens_submission1bpaai.login.LoginActivity
import com.sidratul.storylens_submission1bpaai.user.UserPreferences
import com.sidratul.storylens_submission1bpaai.user.UserViewModel

class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupView()
        setupAction()
        setAnimation()
    }

    private fun setupAction() {
        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignup.setOnClickListener{
            if (valid()) {
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPass.text.toString()
                userViewModel.register(name, email, password)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.check_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun valid() =
        binding.etEmail.error == null && binding.etPass.error == null && binding.etName.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPass.text.isNullOrEmpty() && !binding.etName.text.isNullOrEmpty()

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.icon, View.TRANSLATION_X, -20f, 20f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appIcon = ObjectAnimator.ofFloat(binding.icon, View.ALPHA, 1f).setDuration(700)
        val appName = ObjectAnimator.ofFloat(binding.tvStory, View.ALPHA, 1f).setDuration(700)
        val etName = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(700)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(700)
        val etPass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(700)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(700)
        val txtHaveAc = ObjectAnimator.ofFloat(binding.tvHaveAcc, View.ALPHA, 1f).setDuration(700)
        val txtLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(700)

        val textAnim = AnimatorSet().apply {
            playTogether(appName, txtLogin, txtHaveAc)
        }
        val layoutAnim = AnimatorSet().apply {
            playTogether(etName, etPass, etEmail)
        }

        AnimatorSet().apply {
            playSequentially(
                appIcon,
                textAnim,
                layoutAnim,
                btnSignup
            )
            start()
        }
    }

    private fun setupView() {
        userViewModel.userInfo.observe(this) {
            when (it) {
                is Resource.Success -> {
                    showLoad(false)
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                is Resource.Loading -> showLoad(true)
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoad(false)
                }
            }
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        userViewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]
    }

    private fun showLoad(isLoad: Boolean) {
        if (isLoad){
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }
}