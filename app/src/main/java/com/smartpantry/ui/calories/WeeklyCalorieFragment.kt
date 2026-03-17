package com.smartpantry.ui.calories

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.smartpantry.R
import com.smartpantry.databinding.FragmentWeeklyCaloriesBinding

class WeeklyCalorieFragment : Fragment() {

    private var _binding: FragmentWeeklyCaloriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalorieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyCaloriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBarChart()

        viewModel.weeklyData.observe(viewLifecycleOwner) { data ->
            updateBarChart(data)
            updateStats(data)
        }

        viewModel.loadWeeklyData()
    }

    private fun setupBarChart() {
        binding.barChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            setFitBars(true)
            animateY(800)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.GRAY
            }

            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
                textColor = Color.GRAY
            }

            axisRight.isEnabled = false
            setNoDataText("No calorie data yet")
        }
    }

    private fun updateBarChart(data: List<Pair<String, Int>>) {
        if (data.isEmpty()) return

        val entries = data.mapIndexed { index, (_, calories) ->
            BarEntry(index.toFloat(), calories.toFloat())
        }

        val colors = data.map { (_, calories) ->
            val goal = viewModel.dailyGoal
            val progress = if (goal > 0) (calories * 100) / goal else 0
            when {
                progress < 80 -> ContextCompat.getColor(requireContext(), R.color.calorie_green)
                progress <= 100 -> ContextCompat.getColor(requireContext(), R.color.calorie_yellow)
                else -> ContextCompat.getColor(requireContext(), R.color.calorie_red)
            }
        }

        val dataSet = BarDataSet(entries, "Calories").apply {
            this.colors = colors
            valueTextSize = 10f
            valueTextColor = Color.GRAY
        }

        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.first })
        binding.barChart.data = BarData(dataSet).apply { barWidth = 0.6f }
        binding.barChart.invalidate()
    }

    private fun updateStats(data: List<Pair<String, Int>>) {
        val calories = data.map { it.second }
        val avg = if (calories.isNotEmpty()) calories.average().toInt() else 0
        val highest = calories.maxOrNull() ?: 0
        val lowest = calories.filter { it > 0 }.minOrNull() ?: 0

        binding.tvAvg.text = getString(R.string.avg_weekly, avg)
        binding.tvHighest.text = getString(R.string.kcal_format, highest)
        binding.tvLowest.text = getString(R.string.kcal_format, lowest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
