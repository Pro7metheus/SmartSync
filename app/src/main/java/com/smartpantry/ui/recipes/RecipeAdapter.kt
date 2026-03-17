package com.smartpantry.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartpantry.R
import com.smartpantry.data.model.Recipe
import com.smartpantry.databinding.ItemRecipeBinding

class RecipeAdapter(
    private val onItemClick: (Recipe) -> Unit
) : ListAdapter<Pair<Recipe, Int>, RecipeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<Recipe, Int>) {
            val (recipe, matchPercent) = item
            val context = binding.root.context

            binding.tvRecipeName.text = recipe.name
            binding.tvCategory.text = recipe.category
            binding.tvCalories.text = context.getString(R.string.est_calories, recipe.estimatedCalories)
            binding.tvIngredientsCount.text = "${recipe.ingredients.size} ingredients"

            binding.chipMatch.text = context.getString(R.string.ingredients_match, matchPercent)
            val chipColor = when {
                matchPercent >= 80 -> ContextCompat.getColor(context, R.color.calorie_green)
                matchPercent >= 50 -> ContextCompat.getColor(context, R.color.calorie_yellow)
                else -> ContextCompat.getColor(context, R.color.status_expiring_soon)
            }
            binding.chipMatch.chipBackgroundColor = android.content.res.ColorStateList.valueOf(chipColor)

            binding.root.setOnClickListener { onItemClick(recipe) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pair<Recipe, Int>>() {
        override fun areItemsTheSame(oldItem: Pair<Recipe, Int>, newItem: Pair<Recipe, Int>) =
            oldItem.first.id == newItem.first.id
        override fun areContentsTheSame(oldItem: Pair<Recipe, Int>, newItem: Pair<Recipe, Int>) =
            oldItem == newItem
    }
}
