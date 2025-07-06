package com.example.quizapp.ViewModel

import androidx.lifecycle.ViewModel

class ResultsViewModel : ViewModel() {
    fun saveHighScore(score: Int, sharedPreferences: android.content.SharedPreferences) {
        val currentHighScore = sharedPreferences.getInt("high_score", 0)
        if (score > currentHighScore) {
            sharedPreferences.edit().putInt("high_score", score).apply()
        }
    }
}