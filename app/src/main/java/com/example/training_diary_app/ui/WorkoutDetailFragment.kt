package com.example.training_diary_app.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.training_diary_app.R
import com.example.training_diary_app.data.Workout
import com.example.training_diary_app.viewmodel.WorkoutViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class WorkoutDetailFragment : Fragment(R.layout.fragment_workout_detail) {

    private val workoutViewModel: WorkoutViewModel by viewModels()

    private var workoutId: Int = -1
    private var currentWorkout: Workout? = null

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var buttonEditWorkout: MaterialButton
    private lateinit var buttonDeleteWorkout: MaterialButton

    private lateinit var textDetailDate: TextView
    private lateinit var textDetailTraining: TextView
    private lateinit var textDetailWeight: TextView
    private lateinit var textDetailHasPain: TextView
    private lateinit var textDetailPainDescription: TextView
    private lateinit var textDetailNotes: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutId = requireArguments().getInt("workoutId")

        getViewReferences(view)
        configureToolbar()
        configureButtons()
        observeWorkout()
    }

    private fun getViewReferences(view: View) {
        topAppBar = view.findViewById(R.id.topAppBar)
        buttonEditWorkout = view.findViewById(R.id.buttonEditWorkout)
        buttonDeleteWorkout = view.findViewById(R.id.buttonDeleteWorkout)

        textDetailDate = view.findViewById(R.id.textDetailDate)
        textDetailTraining = view.findViewById(R.id.textDetailTraining)
        textDetailWeight = view.findViewById(R.id.textDetailWeight)
        textDetailHasPain = view.findViewById(R.id.textDetailHasPain)
        textDetailPainDescription = view.findViewById(R.id.textDetailPainDescription)
        textDetailNotes = view.findViewById(R.id.textDetailNotes)
    }

    private fun configureToolbar() {
        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
//
//        topAppBar.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.action_more -> {
//                    true
//                }
//
//                else -> false
//            }
//        }
    }

    private fun configureButtons() {
        buttonEditWorkout.setOnClickListener {
            navigateToEditWorkout()
        }

        buttonDeleteWorkout.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun observeWorkout() {
        workoutViewModel.getWorkoutById(workoutId).observe(viewLifecycleOwner) { workout ->
            currentWorkout = workout

            if (workout == null) {
                findNavController().navigateUp()
                return@observe
            }

            fillWorkoutDetails(workout)
        }
    }

    private fun fillWorkoutDetails(workout: Workout) {
        textDetailDate.text = workout.date
        textDetailTraining.text = workout.training

        textDetailWeight.text = if (workout.bodyWeight != null) {
            "${workout.bodyWeight} kg"
        } else {
            "No weight registered"
        }

        textDetailHasPain.text = if (workout.hasPain) {
            "Yes"
        } else {
            "No"
        }

        textDetailPainDescription.text = if (workout.hasPain) {
            workout.painDescription ?: "Pain reported"
        } else {
            "No pain reported"
        }

        textDetailNotes.text = workout.notes ?: "No notes"
    }

    private fun navigateToEditWorkout() {
        val bundle = Bundle().apply {
            putInt("workoutId", workoutId)
        }

        findNavController().navigate(
            R.id.action_workoutDetailFragment_to_addEditWorkoutFragment,
            bundle
        )
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete workout")
            .setMessage("Are you sure you want to delete this workout?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { _, _ ->
                deleteWorkout()
            }
            .show()
    }

    private fun deleteWorkout() {
        val workout = currentWorkout

        if (workout != null) {
            workoutViewModel.deleteWorkout(workout)
            findNavController().navigateUp()
        }
    }
}