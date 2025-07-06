package com.example.quizapp.ViewModel

import Question
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    private val _questions = MutableLiveData<List<Question>>()
    private val _currentQuestion = MutableLiveData<Question?>()
    private val _currentQuestionIndex = MutableLiveData(0)
    private val _timeLeft = MutableLiveData(30)
    private val _score = MutableLiveData(0)
    private val _quizFinished = MutableLiveData(false)
    private val _isLoading = MutableLiveData(false)
    private val _error = MutableLiveData<String?>()

    val questions: LiveData<List<Question>> = _questions
    val currentQuestion: LiveData<Question?> = _currentQuestion
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex
    val timeLeft: LiveData<Int> = _timeLeft
    val score: LiveData<Int> = _score
    val quizFinished: LiveData<Boolean> = _quizFinished
    val isLoading: LiveData<Boolean> = _isLoading
    val error: LiveData<String?> = _error

    fun startQuiz(category: Int? = null, difficulty: String? = null) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val questions: List<Question> = repository.getQuestions(
                    amount = 10,
                    category = category,
                    difficulty = difficulty
                )
                _questions.value = questions
                _currentQuestion.value = questions.firstOrNull()
                _currentQuestionIndex.value = 0
                _score.value = 0
                _quizFinished.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load questions. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun nextQuestion() {
        val nextIndex = (_currentQuestionIndex.value ?: 0) + 1
        if (nextIndex < (_questions.value?.size ?: 0)) {
            _currentQuestionIndex.value = nextIndex
            _currentQuestion.value = _questions.value?.get(nextIndex)
            _timeLeft.value = 30
        } else {
            _quizFinished.value = true
        }
    }

    fun checkAnswer(selectedAnswer: String): Boolean {
        val isCorrect = _currentQuestion.value?.correct_answer == selectedAnswer
        if (isCorrect) {
            _score.value = (_score.value ?: 0) + 10
        }
        return isCorrect
    }

    fun updateTimeLeft(time: Int) {
        _timeLeft.value = time
    }
}