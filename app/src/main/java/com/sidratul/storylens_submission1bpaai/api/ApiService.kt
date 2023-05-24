package com.sidratul.storylens_submission1bpaai.api

import com.sidratul.storylens_submission1bpaai.login.LoginRequest
import com.sidratul.storylens_submission1bpaai.login.LoginResponse
import com.sidratul.storylens_submission1bpaai.register.RegisterRequest
import com.sidratul.storylens_submission1bpaai.story.BaseResponse
import com.sidratul.storylens_submission1bpaai.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(
        @Body request: RegisterRequest
    ): Call<BaseResponse>

    @POST("login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<BaseResponse>
}