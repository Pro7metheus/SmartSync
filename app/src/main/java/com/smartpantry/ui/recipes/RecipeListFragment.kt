package com.smartpantry.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartpantry.R
import com.smartpantry.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipeAdapter { recipe ->
            val bundle = Bundle().apply { putInt("recipeId", recipe.id) }
            findNavController().navigate(R.id.action_recipeList_to_detail, bundle)
        }
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = adapter

        viewModel.matchedRecipes.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
            binding.layoutEmpty.visibility = if (recipes.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.rvRecipes.visibility = if (recipes.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.loadMatchingRecipes()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMatchingRecipes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
