package com.sidratul.storylens_submission1bpaai.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.sidratul.storylens_submission1bpaai.R
import com.sidratul.storylens_submission1bpaai.ViewModelFactory
import com.sidratul.storylens_submission1bpaai.databinding.ActivityLoginBinding
import com.sidratul.storylens_submission1bpaai.main.MainActivity
import com.sidratul.storylens_submission1bpaai.register.RegisterActivity
import com.sidratul.storylens_submission1bpaai.register.Resource
import com.sidratul.storylens_submission1bpaai.user.UserPreferences
import com.sidratul.storylens_submission1bpaai.user.UserViewModel

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var binding : ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupView()
        setupAction()
        setAnimation()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            if (valid()) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPass.text.toString()
                userViewModel.login(email, password)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.check_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvSignup.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.icon, View.TRANSLATION_X, -20f, 20f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appIcon = ObjectAnimator.ofFloat(binding.icon, View.ALPHA, 1f).setDuration(700)
        val appName = ObjectAnimator.ofFloat(binding.tvStory, View.ALPHA, 1f).setDuration(700)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(700)
        val etPass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(700)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(700)
        val txtHaveAc = ObjectAnimator.ofFloat(binding.tvDontHaveAcc, View.ALPHA, 1f).setDuration(700)
        val txtSignup = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(700)

        val textAnim = AnimatorSet().apply {
            playTogether(appName, txtSignup, txtHaveAc)
        }
        val layoutAnim = AnimatorSet().apply {
            playTogether(etPass, etEmail)
        }

        AnimatorSet().apply {
            playSequentially(
                appIcon,
                textAnim,
                layoutAnim,
                btnLogin
            )
            start()
        }
    }

    private fun setupView() {

        userViewModel.userInfo.observe(this) {
            when (it) {
                is Resource.Success -> {
                    showLoad(false)
                    startActivity(Intent(this, MainActivity::class.java))
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

    private fun valid() =
        binding.etEmail.error == null && binding.etPass.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPass.text.isNullOrEmpty()

}