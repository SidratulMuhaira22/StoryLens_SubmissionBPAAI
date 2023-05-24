package com.sidratul.storylens_submission1bpaai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidratul.storylens_submission1bpaai.main.MainViewModel
import com.sidratul.storylens_submission1bpaai.story.StoryAddViewModel
import com.sidratul.storylens_submission1bpaai.user.UserPreferences
import com.sidratul.storylens_submission1bpaai.user.UserViewModel

class ViewModelFactory(private val pref: UserPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(StoryAddViewModel::class.java)) {
            return StoryAddViewModel(pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.simpleName)
    }
}