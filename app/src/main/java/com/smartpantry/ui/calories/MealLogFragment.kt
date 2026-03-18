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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.smartpantry.R
import com.smartpantry.data.model.PantryItem
import com.smartpantry.data.model.usda.UsdaFood
import com.smartpantry.databinding.FragmentMealLogBinding
import kotlinx.coroutines.launch

sealed class Suggestion {
    abstract val displayText: String
    data class PantrySuggestion(val item: PantryItem) : Suggestion() {
        override val displayText = "${item.name} (${item.caloriesPerUnit} kcal/${item.unit}) - Pantry"
    }
    data class UsdaSuggestion(val food: UsdaFood) : Suggestion() {
        override val displayText: String get() {
            val name = food.description.lowercase().replaceFirstChar { it.uppercase() }
            val brand = if (!food.brandOwner.isNullOrBlank()) {
                val b = food.brandOwner.lowercase().split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { char -> char.uppercase() }
                }
                "($b) "
            } else ""
            val sizeStr = if (food.servingSize != null) {
                if (food.servingSize % 1.0 == 0.0) food.servingSize.toInt().toString() else food.servingSize.toString()
            } else "1"
            val unitStr = food.servingSizeUnit?.lowercase() ?: "serving"
            return "$name $brand- $sizeStr $unitStr - USDA"
        }
    }
    override fun toString(): String = displayText
}

class MealLogFragment : Fragment() {

    private var _binding: FragmentMealLogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalorieViewModel by viewModels()
    private lateinit var adapter: MealLogAdapter
    private var selectedSuggestion: Suggestion? = null
    private var pantryItemsList: List<PantryItem> = emptyList()
    private var currentSuggestions = mutableListOf<Suggestion>()
    private var dropdownAdapter: ArrayAdapter<Suggestion>? = null

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
        dropdownAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            currentSuggestions
        )
        binding.dropdownItem.setAdapter(dropdownAdapter)

        viewModel.pantryItems.observe(viewLifecycleOwner) { items ->
            pantryItemsList = items
            updateDropdownSuggestions()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usdaSearchResults.collect { results ->
                    updateDropdownSuggestions(results)
                    if (results.isNotEmpty() && binding.dropdownItem.hasFocus()) {
                        binding.dropdownItem.showDropDown()
                    }
                }
            }
        }

        binding.dropdownItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                if (binding.dropdownItem.hasFocus()) {
                    updateDropdownSuggestions()
                    viewModel.searchUsdaFoods(query)
                }
            }
        })

        binding.dropdownItem.setOnItemClickListener { parent, _, position, _ ->
            val suggestion = parent.getItemAtPosition(position) as Suggestion
            selectedSuggestion = suggestion
            val textToSet = when(suggestion) {
                is Suggestion.PantrySuggestion -> suggestion.item.name
                is Suggestion.UsdaSuggestion -> suggestion.food.description.lowercase().replaceFirstChar { it.uppercase() }
            }
            binding.dropdownItem.setText(textToSet, false)
            viewModel.clearUsdaSearch()
            updateCaloriePreview()
        }
    }

    private fun updateDropdownSuggestions(usdaResults: List<UsdaFood> = viewModel.usdaSearchResults.value) {
        val query = binding.dropdownItem.text.toString().lowercase()
        val filteredPantry = if (query.isEmpty()) pantryItemsList else pantryItemsList.filter { it.name.lowercase().contains(query) }
        
        currentSuggestions.clear()
        currentSuggestions.addAll(filteredPantry.map { Suggestion.PantrySuggestion(it) })
        currentSuggestions.addAll(usdaResults.map { Suggestion.UsdaSuggestion(it) })
        
        dropdownAdapter?.notifyDataSetChanged()
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
        val suggestion = selectedSuggestion
        val qty = binding.etQuantity.text.toString().toDoubleOrNull() ?: 0.0
        if (suggestion != null && qty > 0) {
            val cal = when (suggestion) {
                is Suggestion.PantrySuggestion -> (suggestion.item.caloriesPerUnit * qty).toInt()
                is Suggestion.UsdaSuggestion -> (suggestion.food.energyKcal * qty).toInt()
            }
            binding.tvCaloriePreview.text = "= $cal kcal"
        } else {
            binding.tvCaloriePreview.text = "= 0 kcal"
        }
    }

    private fun setupLogButton() {
        binding.btnLogMeal.setOnClickListener {
            val suggestion = selectedSuggestion
            val qty = binding.etQuantity.text.toString().toDoubleOrNull()

            if (suggestion == null) {
                Snackbar.make(binding.root, "Please select an item", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (qty == null || qty <= 0) {
                Snackbar.make(binding.root, "Enter a valid quantity", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (suggestion) {
                is Suggestion.PantrySuggestion -> {
                    viewModel.logMeal(suggestion.item, qty)
                }
                is Suggestion.UsdaSuggestion -> {
                    val name = suggestion.food.description.lowercase().replaceFirstChar { it.uppercase() }
                    val baseCals = suggestion.food.energyKcal
                    val cals = (baseCals * qty).toInt()
                    
                    val sizeStr = if (suggestion.food.servingSize != null) {
                        if (suggestion.food.servingSize % 1.0 == 0.0) suggestion.food.servingSize.toInt().toString() else suggestion.food.servingSize.toString()
                    } else "1"
                    val unitStr = suggestion.food.servingSizeUnit?.lowercase() ?: "serving"
                    val fullUnit = "$sizeStr $unitStr"

                    viewModel.logCustomMeal(name, cals, qty, fullUnit)
                }
            }

            binding.dropdownItem.setText("", false)
            binding.etQuantity.setText("")
            selectedSuggestion = null
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
