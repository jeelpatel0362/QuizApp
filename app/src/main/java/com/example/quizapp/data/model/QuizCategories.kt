package com.example.quizapp.data.model

enum class QuizCategories(val id: Int, val displayName: String) {
    GENERAL_KNOWLEDGE(9, "General Knowledge"),
    SCIENCE_COMPUTERS(18, "Science: Computers"),
    SCIENCE_MATHEMATICS(19, "Science: Mathematics"),
    MYTHOLOGY(20, "Mythology"),
    SPORTS(21, "Sports"),
    GEOGRAPHY(22, "Geography"),
    HISTORY(23, "History"),
    ANIMALS(27, "Animals");
}