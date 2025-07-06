package com.example.quizapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.data.repository.QuizRepository

class ViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                QuizViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> {
                CategoriesViewModel() as T
            }
            modelClass.isAssignableFrom(ResultsViewModel::class.java) -> {
                ResultsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}