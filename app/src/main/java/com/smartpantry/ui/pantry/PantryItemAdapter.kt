package com.smartpantry.ui.pantry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartpantry.R
import com.smartpantry.data.model.PantryItem
import com.smartpantry.databinding.ItemPantryBinding
import com.smartpantry.utils.DateUtils

class PantryItemAdapter(
    private val onItemClick: (PantryItem) -> Unit
) : ListAdapter<PantryItem, PantryItemAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPantryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemPantryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PantryItem) {
            val context = binding.root.context

            binding.tvItemName.text = item.name
            binding.chipCategory.text = item.category
            binding.tvQuantity.text = "${item.quantity} ${item.unit}"

            if (item.caloriesPerUnit > 0) {
                binding.tvCalories.text = "${item.caloriesPerUnit} kcal/${item.unit}"
            } else {
                binding.tvCalories.text = ""
            }

            // Expiry status
            val daysLeft = DateUtils.getDaysUntilExpiry(item.expiryDate)
            when {
                DateUtils.isExpired(item.expiryDate) -> {
                    binding.tvExpiry.text = context.getString(R.string.expired)
                    binding.tvExpiry.setTextColor(ContextCompat.getColor(context, R.color.status_expired))
                    binding.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_expired))
                }
                DateUtils.isExpiringSoon(item.expiryDate) -> {
                    binding.tvExpiry.text = context.getString(R.string.days_left, daysLeft.toInt())
                    binding.tvExpiry.setTextColor(ContextCompat.getColor(context, R.color.status_expiring_soon))
                    binding.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_expiring_soon))
                }
                else -> {
                    if (item.expiryDate > 0) {
                        binding.tvExpiry.text = context.getString(R.string.days_left, daysLeft.toInt())
                    } else {
                        binding.tvExpiry.text = "No expiry"
                    }
                    binding.tvExpiry.setTextColor(ContextCompat.getColor(context, R.color.status_fresh))
                    binding.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_fresh))
                }
            }

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PantryItem>() {
        override fun areItemsTheSame(oldItem: PantryItem, newItem: PantryItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PantryItem, newItem: PantryItem) = oldItem == newItem
    }
}
