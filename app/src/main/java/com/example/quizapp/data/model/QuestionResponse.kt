package com.example.quizapp.data.model

import Question

data class QuestionResponse(
    val response_code: Int,
    val results: List<Question>
)