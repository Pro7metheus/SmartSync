package com.smartpantry.ui.pantry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.smartpantry.R
import com.smartpantry.data.model.Category
import com.smartpantry.databinding.FragmentPantryListBinding

class PantryListFragment : Fragment() {

    private var _binding: FragmentPantryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PantryViewModel by viewModels()
    private lateinit var adapter: PantryItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPantryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupCategoryChips()
        setupFab()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = PantryItemAdapter { item ->
            val bundle = Bundle().apply { putLong("itemId", item.id) }
            findNavController().navigate(R.id.action_pantryList_to_addEdit, bundle)
        }
        binding.rvPantryItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPantryItems.adapter = adapter

        // Swipe to delete
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                viewModel.deleteItem(item)
                Snackbar.make(binding.root, R.string.item_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { viewModel.insertItem(item) }
                    .show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvPantryItems)
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupCategoryChips() {
        val categories = listOf("All") + Category.ALL
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = category == "All"
            }
            binding.chipGroupCategories.addView(chip)
        }

        binding.chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds.first())
                viewModel.setCategory(chip?.text?.toString() ?: "All")
            }
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_pantryList_to_addEdit)
        }
    }

    private fun observeData() {
        viewModel.filteredItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            binding.layoutEmpty.visibility = if (items.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.rvPantryItems.visibility = if (items.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
