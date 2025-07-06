package com.example.quizapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.model.QuizCategories

class CategoriesViewModel : ViewModel() {
    private var selectedCategory: Int? = null
    private var selectedDifficulty: String? = null

    fun setSelectedCategory(categoryId: Int?) {
        selectedCategory = categoryId
    }

    fun setSelectedDifficulty(difficulty: String?) {
        selectedDifficulty = difficulty
    }

    fun getSelectedCategory() = selectedCategory
    fun getSelectedDifficulty() = selectedDifficulty

    fun getCategories(): List<QuizCategories> = QuizCategories.values().toList()
}