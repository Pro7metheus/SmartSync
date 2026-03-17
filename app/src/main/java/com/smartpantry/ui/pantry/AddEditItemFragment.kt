package com.smartpantry.ui.pantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.smartpantry.R
import com.smartpantry.SmartPantryApp
import com.smartpantry.data.model.Category
import com.smartpantry.data.model.PantryItem
import com.smartpantry.data.model.UnitType
import com.smartpantry.data.model.usda.UsdaFood
import com.smartpantry.databinding.FragmentAddEditItemBinding
import com.smartpantry.utils.DateUtils
import kotlinx.coroutines.launch

class AddEditItemFragment : Fragment() {

    private var _binding: FragmentAddEditItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PantryViewModel by viewModels()

    private var editItemId: Long = -1L
    private var purchaseDate: Long = System.currentTimeMillis()
    private var expiryDate: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editItemId = arguments?.getLong("itemId", -1L) ?: -1L

        setupDropdowns()
        setupDatePickers()
        setupSaveButton()
        setupAutocomplete()

        if (editItemId != -1L) {
            binding.tvTitle.text = getString(R.string.edit_item)
            loadItem(editItemId)
        }

        // Default purchase date
        binding.etPurchaseDate.setText(DateUtils.formatDate(purchaseDate))
    }

    private fun setupDropdowns() {
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Category.ALL
        )
        binding.dropdownCategory.setAdapter(categoryAdapter)

        val unitAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            UnitType.ALL
        )
        binding.dropdownUnit.setAdapter(unitAdapter)
    }

    private fun setupDatePickers() {
        binding.etPurchaseDate.setOnClickListener {
            showDatePicker("Purchase Date") { timestamp ->
                purchaseDate = timestamp
                binding.etPurchaseDate.setText(DateUtils.formatDate(timestamp))
            }
        }

        binding.etExpiryDate.setOnClickListener {
            showDatePicker("Expiry Date") { timestamp ->
                expiryDate = timestamp
                binding.etExpiryDate.setText(DateUtils.formatDate(timestamp))
            }
        }
    }

    private fun showDatePicker(title: String, onDateSelected: (Long) -> Unit) {
        if (parentFragmentManager.findFragmentByTag(title) != null) return

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        picker.addOnPositiveButtonClickListener { onDateSelected(it) }
        picker.show(parentFragmentManager, title)
    }

    private fun setupAutocomplete() {
        // Observe search results
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usdaSearchResults.collect { results ->
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        results.map { it.description.lowercase().replaceFirstChar { char -> char.uppercase() } }
                    )
                    binding.etName.setAdapter(adapter)

                    // Show dropdown if we have focus and results
                    if (results.isNotEmpty() && binding.etName.hasFocus()) {
                        binding.etName.showDropDown()
                    }
                }
            }
        }

        // Add text watcher to trigger search (the debounce is handled in ViewModel)
        binding.etName.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val query = s?.toString() ?: ""
                // Only search if user is typing (not when an item was just selected)
                if (binding.etName.hasFocus()) {
                    val nlpResult = com.smartpantry.utils.NlpParser.parseInput(query)
                    viewModel.searchUsdaFoods(nlpResult.foodName)
                }
            }
        })

        // Handle item selection from dropdown
        binding.etName.setOnItemClickListener { parent, _, position, _ ->
            val selectedText = parent.getItemAtPosition(position) as String
            val selectedFood = viewModel.usdaSearchResults.value.find {
                it.description.lowercase().replaceFirstChar { char -> char.uppercase() } == selectedText
            }
            
            if (selectedFood != null) {
                // Parse original input for quantity/unit before setting Name
                val currentInput = binding.etName.text.toString()
                val nlpResult = com.smartpantry.utils.NlpParser.parseInput(currentInput)

                // Set Name
                binding.etName.setText(selectedFood.description.lowercase().replaceFirstChar { char -> char.uppercase() }, false)
                
                // Clear the dropdown results so it doesn't pop up again
                viewModel.clearUsdaSearch()

                // Set Quantity and Unit using NLP results or fallback to API defaults
                val finalQuantity = nlpResult.quantity ?: selectedFood.servingSize ?: 1.0
                val finalUnit = nlpResult.unit ?: selectedFood.servingSizeUnit?.let { unit ->
                    when (unit.lowercase()) {
                        "g", "gr", "gram" -> "g"
                        "ml", "milliliter" -> "ml"
                        "oz", "ounce" -> "oz"
                        else -> "pieces"
                    }
                } ?: "pieces"

                binding.etQuantity.setText(if (finalQuantity % 1.0 == 0.0) finalQuantity.toInt().toString() else finalQuantity.toString())
                binding.dropdownUnit.setText(finalUnit, false)

                // Set Calories (scale if user provided a specific quantity and USDA gives a serving size)
                val baseCals = selectedFood.energyKcal
                if (baseCals > 0) {
                    val calsToSet = if (nlpResult.quantity != null && selectedFood.servingSize != null && selectedFood.servingSize > 0) {
                        (baseCals / selectedFood.servingSize) * nlpResult.quantity
                    } else {
                        baseCals
                    }
                    binding.etCalories.setText(calsToSet.toInt().toString())
                }
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val category = binding.dropdownCategory.text.toString().trim()
            val quantityStr = binding.etQuantity.text.toString().trim()
            val unit = binding.dropdownUnit.text.toString().trim()
            val minQuantityStr = binding.etMinQuantity.text.toString().trim()
            val caloriesStr = binding.etCalories.text.toString().trim()

            // Validation
            if (name.isEmpty()) {
                binding.tilName.error = "Name is required"
                return@setOnClickListener
            }
            binding.tilName.error = null

            if (category.isEmpty()) {
                binding.tilCategory.error = "Category is required"
                return@setOnClickListener
            }
            binding.tilCategory.error = null

            if (quantityStr.isEmpty()) {
                binding.tilQuantity.error = "Quantity is required"
                return@setOnClickListener
            }
            binding.tilQuantity.error = null

            if (unit.isEmpty()) {
                binding.tilUnit.error = "Unit is required"
                return@setOnClickListener
            }
            binding.tilUnit.error = null

            val quantity = quantityStr.toDoubleOrNull() ?: 0.0
            val minQuantity = minQuantityStr.toDoubleOrNull() ?: 1.0
            val calories = caloriesStr.toIntOrNull() ?: 0

            val app = requireActivity().application as SmartPantryApp
            val userId = app.authManager.currentUserId

            val item = PantryItem(
                id = if (editItemId != -1L) editItemId else 0,
                name = name,
                category = category,
                quantity = quantity,
                unit = unit,
                minQuantity = minQuantity,
                purchaseDate = purchaseDate,
                expiryDate = expiryDate,
                caloriesPerUnit = calories,
                userId = userId
            )

            if (editItemId != -1L) {
                viewModel.updateItem(item)
            } else {
                viewModel.insertItem(item)
            }

            findNavController().navigateUp()
        }
    }

    private fun loadItem(id: Long) {
        lifecycleScope.launch {
            val item = viewModel.getItemById(id)
            item?.let {
                binding.etName.setText(it.name)
                binding.dropdownCategory.setText(it.category, false)
                binding.etQuantity.setText(it.quantity.toString())
                binding.dropdownUnit.setText(it.unit, false)
                binding.etMinQuantity.setText(it.minQuantity.toString())
                binding.etCalories.setText(if (it.caloriesPerUnit > 0) it.caloriesPerUnit.toString() else "")

                purchaseDate = it.purchaseDate
                expiryDate = it.expiryDate
                binding.etPurchaseDate.setText(DateUtils.formatDate(it.purchaseDate))
                if (it.expiryDate > 0) {
                    binding.etExpiryDate.setText(DateUtils.formatDate(it.expiryDate))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
