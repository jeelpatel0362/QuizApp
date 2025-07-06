package com.example.quizapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.ViewModel.HomeViewModel
import com.example.quizapp.ViewModel.ViewModelFactory
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val factory = ViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        val sharedPref = requireActivity().getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
        viewModel.loadHighScore(sharedPref)

        lifecycleScope.launch {
            viewModel.highScore.collectLatest { highScore ->
                view.findViewById<TextView>(R.id.high_score_text)?.text = "High Score: $highScore"
            }
        }

        view.findViewById<Button>(R.id.start_quiz_button).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
        }

        return view
    }
}