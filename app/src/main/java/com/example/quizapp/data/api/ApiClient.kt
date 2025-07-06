package com.example.quizapp.data.api

import com.example.quizapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val triviaApiService: TriviaApiService by lazy {
        retrofit.create(TriviaApiService::class.java)
    }
}