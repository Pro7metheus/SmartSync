package com.smartpantry.ui.calories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartpantry.data.model.MealLog
import com.smartpantry.databinding.ItemMealLogBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MealLogAdapter : ListAdapter<MealLog, MealLogAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMealLogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMealLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        fun bind(mealLog: MealLog) {
            binding.tvMealName.text = mealLog.itemName
            binding.tvMealQuantity.text = "${mealLog.quantityConsumed} ${mealLog.unit}"
            binding.tvMealCalories.text = "${mealLog.caloriesConsumed} kcal"
            binding.tvMealTime.text = timeFormat.format(Date(mealLog.timestamp))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MealLog>() {
        override fun areItemsTheSame(oldItem: MealLog, newItem: MealLog) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MealLog, newItem: MealLog) = oldItem == newItem
    }
}
