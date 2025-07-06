package com.example.quizapp.data.api

import com.example.quizapp.data.model.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String = "multiple"
    ): Response<QuestionResponse>
}