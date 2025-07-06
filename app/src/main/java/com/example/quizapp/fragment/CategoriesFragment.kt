package com.example.quizapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.ViewModel.CategoriesViewModel
import com.example.quizapp.ViewModel.ViewModelFactory
import com.example.quizapp.data.repository.QuizRepository


class CategoriesFragment : Fragment() {
    private lateinit var viewModel: CategoriesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, factory).get(CategoriesViewModel::class.java)

        val categoriesRadioGroup = view.findViewById<RadioGroup>(R.id.categories_radio_group)
        val difficultyRadioGroup = view.findViewById<RadioGroup>(R.id.difficulty_radio_group)

        viewModel.getCategories().forEach { category ->
            RadioButton(requireContext()).apply {
                id = category.id
                text = category.displayName
                categoriesRadioGroup.addView(this)
            }
        }

        categoriesRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setSelectedCategory(checkedId)
        }

        difficultyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val difficulty = when (checkedId) {
                R.id.easy_radio -> "easy"
                R.id.medium_radio -> "medium"
                R.id.hard_radio -> "hard"
                else -> null
            }
            viewModel.setSelectedDifficulty(difficulty)
        }

        view.findViewById<Button>(R.id.start_quiz_button).setOnClickListener {
            val selectedCategory = viewModel.getSelectedCategory()
            if (selectedCategory == null) {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val args = Bundle().apply {
                putInt("selectedCategory", selectedCategory)
                putString("selectedDifficulty", viewModel.getSelectedDifficulty() ?: "")
            }
            findNavController().navigate(R.id.action_categoriesFragment_to_quizFragment, args)
        }
    }
}