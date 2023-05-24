package com.sidratul.storylens_submission1bpaai.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.sidratul.storylens_submission1bpaai.ViewModelFactory
import com.sidratul.storylens_submission1bpaai.register.Resource
import com.sidratul.storylens_submission1bpaai.databinding.ActivityMainBinding
import com.sidratul.storylens_submission1bpaai.login.LoginActivity
import com.sidratul.storylens_submission1bpaai.story.StoryAdapter
import com.sidratul.storylens_submission1bpaai.story.StoryAddActivity
import com.sidratul.storylens_submission1bpaai.user.UserPreferences
import com.sidratul.storylens_submission1bpaai.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupView()
        onClick()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    private fun onClick() {
        binding.logOut.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
        binding.addStory.setOnClickListener{
            startActivity(Intent(this, StoryAddActivity::class.java))
        }
        binding.setting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setupView() {
        storyAdapter = StoryAdapter()

        userViewModel.getUserToken().observe(this){ token ->
            if (token.isNotEmpty()){
                mainViewModel.stories.observe(this) {
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let { stories -> storyAdapter.setData(stories) }
                            showLoad(false)
                        }
                        is Resource.Loading -> showLoad(true)
                        is Resource.Error -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getStories()
                }
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        with(binding.rvStory) {
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)

        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        userViewModel= ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

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