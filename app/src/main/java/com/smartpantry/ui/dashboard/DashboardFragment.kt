package com.smartpantry.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.smartpantry.R
import com.smartpantry.SmartPantryApp
import com.smartpantry.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as SmartPantryApp
        binding.tvGreeting.text = "Hello, ${app.authManager.currentUsername}!"

        setupObservers()
        setupPieChart()
        viewModel.loadCategoryData()
    }

    private fun setupObservers() {
        viewModel.totalItemCount.observe(viewLifecycleOwner) {
            binding.tvTotalItems.text = (it ?: 0).toString()
        }
        viewModel.expiringSoonCount.observe(viewLifecycleOwner) {
            binding.tvExpiringSoon.text = (it ?: 0).toString()
        }
        viewModel.lowStockCount.observe(viewLifecycleOwner) {
            binding.tvLowStock.text = (it ?: 0).toString()
        }
        viewModel.outOfStockCount.observe(viewLifecycleOwner) {
            binding.tvOutOfStock.text = (it ?: 0).toString()
        }

        // Calorie progress
        val goal = viewModel.dailyCalorieGoal
        binding.tvCalorieGoal.text = getString(R.string.calorie_goal, goal)

        viewModel.dailyCalorieTotal.observe(viewLifecycleOwner) { total ->
            val calories = total ?: 0
            binding.tvCalorieCount.text = getString(R.string.kcal_format, calories)

            val progress = if (goal > 0) (calories * 100) / goal else 0
            binding.progressCalories.progress = progress.coerceAtMost(100)

            // Color based on percentage
            val color = when {
                progress < 80 -> ContextCompat.getColor(requireContext(), R.color.calorie_green)
                progress <= 100 -> ContextCompat.getColor(requireContext(), R.color.calorie_yellow)
                else -> ContextCompat.getColor(requireContext(), R.color.calorie_red)
            }
            binding.progressCalories.progressTintList = android.content.res.ColorStateList.valueOf(color)
        }

        // Category pie chart
        viewModel.categoryData.observe(viewLifecycleOwner) { data ->
            updatePieChart(data)
        }
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            holeRadius = 45f
            transparentCircleRadius = 50f
            setDrawEntryLabels(true)
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(10f)
            legend.isEnabled = false
            setNoDataText("No items in pantry yet")
            animateY(800)
        }
    }

    private fun updatePieChart(data: Map<String, Int>) {
        if (data.isEmpty()) return

        val entries = data.map { PieEntry(it.value.toFloat(), it.key) }
        val colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.category_dairy),
            ContextCompat.getColor(requireContext(), R.color.category_grains),
            ContextCompat.getColor(requireContext(), R.color.category_vegetables),
            ContextCompat.getColor(requireContext(), R.color.category_fruits),
            ContextCompat.getColor(requireContext(), R.color.category_beverages),
            ContextCompat.getColor(requireContext(), R.color.category_spices),
            ContextCompat.getColor(requireContext(), R.color.category_meat),
            ContextCompat.getColor(requireContext(), R.color.category_snacks),
            ContextCompat.getColor(requireContext(), R.color.category_condiments),
            ContextCompat.getColor(requireContext(), R.color.category_other)
        )

        val dataSet = PieDataSet(entries, "Categories").apply {
            this.colors = colors
            sliceSpace = 2f
            valueTextSize = 12f
            valueFormatter = PercentFormatter(binding.pieChart)
        }

        binding.pieChart.data = PieData(dataSet)
        binding.pieChart.invalidate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCategoryData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
