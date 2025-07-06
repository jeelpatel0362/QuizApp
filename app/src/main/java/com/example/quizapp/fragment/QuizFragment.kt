package com.example.quizapp.fragment

import Question
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.quizapp.R
import com.example.quizapp.ViewModel.QuizViewModel
import com.example.quizapp.ViewModel.ViewModelFactory

import com.example.quizapp.data.repository.QuizRepository
import com.example.quizapp.utils.decodeHtmlEntities
import com.example.quizapp.utils.showToast

class QuizFragment : Fragment() {

    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var retryButton: Button

    private lateinit var viewModel: QuizViewModel
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        initializeViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        setupListeners()
        startQuiz()
    }

    private fun initializeViews(view: View) {
        questionText = view.findViewById(R.id.question_text)
        timerText = view.findViewById(R.id.timer_text)
        answersRadioGroup = view.findViewById(R.id.answers_radio_group)
        submitButton = view.findViewById(R.id.submit_button)
        progressBar = view.findViewById(R.id.progress_bar)
        errorText = view.findViewById(R.id.error_text)
        retryButton = view.findViewById(R.id.retry_button)
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, factory).get(QuizViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            question?.let {
                displayQuestion(it)
            }
        }

        viewModel.timeLeft.observe(viewLifecycleOwner) { time ->
            updateTimerDisplay(time)
            if (time == 30) startTimer()
        }

        viewModel.quizFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) navigateToResults()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
            } ?: hideError()
        }

        viewModel.score.observe(viewLifecycleOwner) { score ->

        }
    }

    private fun setupListeners() {
        submitButton.setOnClickListener {
            handleAnswerSubmission()
        }

        retryButton.setOnClickListener {
            retryQuiz()
        }
    }

    private fun startQuiz() {
        val selectedCategory = arguments?.getInt("selectedCategory", -1).takeIf { it != -1 }
        val selectedDifficulty = arguments?.getString("selectedDifficulty")
        viewModel.startQuiz(selectedCategory, selectedDifficulty)
    }

    private fun displayQuestion(question: Question) {
        questionText.text = question.question.decodeHtmlEntities()
        answersRadioGroup.removeAllViews()
        question.decodedAllAnswers.forEach { answer ->
            RadioButton(requireContext()).apply {
                text = answer
                id = View.generateViewId()
                answersRadioGroup.addView(this)
            }
        }
    }

    private fun updateTimerDisplay(time: Int) {
        timerText.text = getString(R.string.time_left, time)
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.updateTimeLeft((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                showToast("Time's up! Moving to next question")
                viewModel.nextQuestion()
            }
        }.start()
    }

    private fun handleAnswerSubmission() {
        val selectedId = answersRadioGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedAnswer = requireView().findViewById<RadioButton>(selectedId).text.toString()
            val isCorrect = viewModel.checkAnswer(selectedAnswer)
            showToast(if (isCorrect) "Correct!" else "Wrong answer!")
            viewModel.nextQuestion()
        } else {
            showToast("Please select an answer")
        }
    }

    private fun retryQuiz() {
        val selectedCategory = arguments?.getInt("selectedCategory", -1).takeIf { it != -1 }
        val selectedDifficulty = arguments?.getString("selectedDifficulty")
        viewModel.startQuiz(selectedCategory, selectedDifficulty)
    }

    private fun showError(error: String) {
        errorText.text = error
        errorText.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorText.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun navigateToResults() {
        val score = viewModel.score.value ?: 0
        val totalQuestions = viewModel.questions.value?.size ?: 10
        val correctAnswers = score / 10
        val wrongAnswers = totalQuestions - correctAnswers

        val args = Bundle().apply {
            putInt("score", score)
            putInt("correctAnswers", correctAnswers)
            putInt("wrongAnswers", wrongAnswers)
        }
        findNavController().navigate(
            R.id.action_quizFragment_to_resultsFragment,
            args
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        answersRadioGroup.removeAllViews()
    }
}