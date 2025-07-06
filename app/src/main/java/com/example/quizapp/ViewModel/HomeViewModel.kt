package com.example.quizapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(private val repository: QuizRepository) : ViewModel() {
    private val _highScore = MutableStateFlow(0)
    val highScore: StateFlow<Int> = _highScore

    fun loadHighScore(sharedPreferences: android.content.SharedPreferences) {
        _highScore.value = sharedPreferences.getInt("high_score", 0)
    }
}