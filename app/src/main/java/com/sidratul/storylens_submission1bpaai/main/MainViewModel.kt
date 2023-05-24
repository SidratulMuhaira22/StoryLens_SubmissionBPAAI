package com.sidratul.storylens_submission1bpaai.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sidratul.storylens_submission1bpaai.register.Resource
import com.google.gson.Gson
import com.sidratul.storylens_submission1bpaai.api.ApiConfig
import com.sidratul.storylens_submission1bpaai.story.BaseResponse
import com.sidratul.storylens_submission1bpaai.story.StoryModel
import com.sidratul.storylens_submission1bpaai.story.StoryResponse
import com.sidratul.storylens_submission1bpaai.user.UserPreferences
import kotlinx.coroutines.flow.first
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreferences) : ViewModel(){
    private val _stories = MutableLiveData<Resource<ArrayList<StoryModel>>>()
    val stories: LiveData<Resource<ArrayList<StoryModel>>> = _stories

    suspend fun getStories() {
        _stories.postValue(Resource.Loading())
        val client =
            ApiConfig.getApiClient().getStories(token = "Bearer ${pref.getToken().first()}")

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        val listStory = it.listStory
                        _stories.postValue(Resource.Success(ArrayList(listStory)))
                    }
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _stories.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(
                    MainViewModel::class.java.simpleName,
                    "onFailure getStories"
                )
                _stories.postValue(Resource.Error(t.message))
            }
        })
    }
}