package com.example.quizapp.data.repository

import Question
import com.example.quizapp.data.api.ApiClient
import com.example.quizapp.utils.Constants
import retrofit2.HttpException
import java.io.IOException

class QuizRepository {
    suspend fun getQuestions(
        amount: Int = Constants.DEFAULT_QUESTION_COUNT,
        category: Int? = null,
        difficulty: String? = null
    ): List<Question> {
        return try {
            val response = ApiClient.triviaApiService.getQuestions(
                amount = amount.coerceIn(1, 50), // Validate amount range
                category = category,
                difficulty = difficulty
            )

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            response.body()?.results?.takeIf { it.isNotEmpty() }
                ?: throw Exception("No questions available")

        } catch (e: IOException) {
            throw IOException("Network unavailable. Please check your connection.")
        } catch (e: HttpException) {
            throw Exception("Server error: ${e.code()}")
        } catch (e: Exception) {
            throw Exception("Failed to load questions: ${e.message}")
        }
    }
}