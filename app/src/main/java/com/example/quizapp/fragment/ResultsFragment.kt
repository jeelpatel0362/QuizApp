package com.example.quizapp.fragment

import Question
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.ViewModel.ResultsViewModel
import com.example.quizapp.ViewModel.ViewModelFactory
import com.example.quizapp.data.repository.QuizRepository
import com.example.quizapp.utils.decodeHtmlEntities

class ResultsFragment : Fragment() {
    private lateinit var viewModel: ResultsViewModel
    private lateinit var questionsContainer: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        val factory = ViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, factory).get(ResultsViewModel::class.java)

        val score = arguments?.getInt("score") ?: 0
        val correctAnswers = arguments?.getInt("correctAnswers") ?: 0
        val wrongAnswers = arguments?.getInt("wrongAnswers") ?: 0
        val questions = arguments?.getParcelableArrayList<Question>("questions")

        view.findViewById<TextView>(R.id.result_title).text =
            if(score > 50) "Congratulations!" else "Quiz Completed"

        view.findViewById<TextView>(R.id.score_text).text = "Score: $score"
        view.findViewById<TextView>(R.id.correct_answers_text).text = "Correct: $correctAnswers"
        view.findViewById<TextView>(R.id.wrong_answers_text).text = "Wrong: $wrongAnswers"

        questionsContainer = view.findViewById(R.id.questions_container)
        questions?.let { displayQuestionsWithAnswers(it) }

        val sharedPref = requireActivity().getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
        viewModel.saveHighScore(score, sharedPref)

        view.findViewById<Button>(R.id.play_again_button).setOnClickListener {
            findNavController().popBackStack()
        }

        view.findViewById<Button>(R.id.go_home_button).setOnClickListener {
            findNavController().navigate(R.id.action_resultsFragment_to_homeFragment)
        }

        return view
    }

    private fun displayQuestionsWithAnswers(questions: List<Question>) {
        questions.forEach { question ->
            val questionView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_question_result, questionsContainer, false)

            questionView.findViewById<TextView>(R.id.question_text).text =
                question.question.decodeHtmlEntities()
            questionView.findViewById<TextView>(R.id.correct_answer_text).text =
                "Correct answer: ${question.correct_answer.decodeHtmlEntities()}"

            questionsContainer.addView(questionView)
        }
    }
}