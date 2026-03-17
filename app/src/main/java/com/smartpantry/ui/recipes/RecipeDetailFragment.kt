package com.smartpantry.ui.recipes

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.smartpantry.R
import com.smartpantry.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt("recipeId", -1) ?: -1
        if (recipeId == -1) return

        val recipe = viewModel.getRecipeById(recipeId) ?: return

        binding.tvRecipeName.text = recipe.name
        binding.chipCategory.text = recipe.category
        binding.tvEstCalories.text = getString(R.string.est_calories, recipe.estimatedCalories)
        binding.tvInstructions.text = recipe.instructions

        // Load ingredients with availability highlighting
        lifecycleScope.launch {
            val pantryNames = viewModel.getCurrentPantryNames()
            recipe.ingredients.forEach { ingredient ->
                val isAvailable = pantryNames.any { pantryName ->
                    pantryName.contains(ingredient.lowercase().trim()) ||
                    ingredient.lowercase().trim().contains(pantryName)
                }
                addIngredientView(ingredient, isAvailable)
            }
        }
    }

    private fun addIngredientView(ingredient: String, isAvailable: Boolean) {
        val tv = TextView(requireContext()).apply {
            text = if (isAvailable) "✓  $ingredient" else "✗  $ingredient"
            textSize = 15f
            setPadding(0, 8, 0, 8)
            if (isAvailable) {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.status_fresh))
                setTypeface(null, Typeface.BOLD)
            } else {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_outline))
            }
        }
        binding.layoutIngredients.addView(tv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
