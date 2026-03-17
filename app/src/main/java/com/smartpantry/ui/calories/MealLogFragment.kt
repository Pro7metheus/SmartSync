package com.smartpantry.ui.calories

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.smartpantry.R
import com.smartpantry.data.model.PantryItem
import com.smartpantry.databinding.FragmentMealLogBinding

class MealLogFragment : Fragment() {

    private var _binding: FragmentMealLogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalorieViewModel by viewModels()
    private lateinit var adapter: MealLogAdapter
    private var selectedItem: PantryItem? = null
    private var pantryItemsList: List<PantryItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupItemDropdown()
        setupQuantityInput()
        setupLogButton()
        setupWeeklyButton()
        observeData()
        viewModel.loadPantryItems()
    }

    private fun setupRecyclerView() {
        adapter = MealLogAdapter()
        binding.rvMealLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMealLogs.adapter = adapter

        // Swipe to delete
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val mealLog = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteMealLog(mealLog)
                Snackbar.make(binding.root, "Meal log deleted", Snackbar.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvMealLogs)
    }

    private fun setupItemDropdown() {
        viewModel.pantryItems.observe(viewLifecycleOwner) { items ->
            pantryItemsList = items
            val names = items.map { "${it.name} (${it.caloriesPerUnit} kcal/${it.unit})" }
            val dropdownAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                names
            )
            binding.dropdownItem.setAdapter(dropdownAdapter)
        }

        binding.dropdownItem.setOnItemClickListener { _, _, position, _ ->
            selectedItem = pantryItemsList.getOrNull(position)
            updateCaloriePreview()
        }
    }

    private fun setupQuantityInput() {
        binding.etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateCaloriePreview()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateCaloriePreview() {
        val item = selectedItem
        val qty = binding.etQuantity.text.toString().toDoubleOrNull() ?: 0.0
        if (item != null && qty > 0) {
            val cal = (item.caloriesPerUnit * qty).toInt()
            binding.tvCaloriePreview.text = "= $cal kcal"
        } else {
            binding.tvCaloriePreview.text = "= 0 kcal"
        }
    }

    private fun setupLogButton() {
        binding.btnLogMeal.setOnClickListener {
            val item = selectedItem
            val qty = binding.etQuantity.text.toString().toDoubleOrNull()

            if (item == null) {
                Snackbar.make(binding.root, "Please select an item", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (qty == null || qty <= 0) {
                Snackbar.make(binding.root, "Enter a valid quantity", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.logMeal(item, qty)
            binding.dropdownItem.setText("", false)
            binding.etQuantity.setText("")
            selectedItem = null
            binding.tvCaloriePreview.text = "= 0 kcal"
            Snackbar.make(binding.root, "Meal logged!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupWeeklyButton() {
        binding.btnWeekly.setOnClickListener {
            findNavController().navigate(R.id.action_mealLog_to_weekly)
        }
    }

    private fun observeData() {
        val goal = viewModel.dailyGoal
        binding.tvGoalInfo.text = getString(R.string.calorie_goal, goal)

        viewModel.todayTotal.observe(viewLifecycleOwner) { total ->
            val calories = total ?: 0
            binding.tvTodayTotal.text = getString(R.string.kcal_format, calories)

            val progress = if (goal > 0) (calories * 100) / goal else 0
            binding.progressDaily.progress = progress.coerceAtMost(100)

            val color = when {
                progress < 80 -> ContextCompat.getColor(requireContext(), R.color.calorie_green)
                progress <= 100 -> ContextCompat.getColor(requireContext(), R.color.calorie_yellow)
                else -> ContextCompat.getColor(requireContext(), R.color.calorie_red)
            }
            binding.progressDaily.progressTintList = android.content.res.ColorStateList.valueOf(color)
        }

        viewModel.todayLogs.observe(viewLifecycleOwner) { logs ->
            adapter.submitList(logs)
            binding.tvEmptyMeals.visibility = if (logs.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.rvMealLogs.visibility = if (logs.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPantryItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
