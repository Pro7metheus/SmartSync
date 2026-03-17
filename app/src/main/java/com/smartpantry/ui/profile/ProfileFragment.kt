package com.smartpantry.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartpantry.R
import com.smartpantry.SmartPantryApp
import com.smartpantry.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as SmartPantryApp
        val authManager = app.authManager

        binding.tvUsername.text = authManager.currentUsername
        binding.etCalorieGoal.setText(authManager.dailyCalorieGoal.toString())

        binding.btnSaveGoal.setOnClickListener {
            val goal = binding.etCalorieGoal.text.toString().toIntOrNull()
            if (goal != null && goal > 0) {
                authManager.dailyCalorieGoal = goal
                Snackbar.make(binding.root, "Calorie goal updated!", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Enter a valid goal", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.yes) { _, _ ->
                    authManager.logout()
                    findNavController().navigate(R.id.action_profile_to_login)
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
