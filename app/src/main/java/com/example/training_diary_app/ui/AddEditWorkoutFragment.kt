package com.example.training_diary_app.ui

import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.training_diary_app.R
import com.example.training_diary_app.data.Workout
import com.example.training_diary_app.viewmodel.WorkoutViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddEditWorkoutFragment : Fragment(R.layout.fragment_add_edit_workout) {

    private val workoutViewModel: WorkoutViewModel by viewModels()

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var editTextDate: TextInputEditText
    private lateinit var editTextTraining: TextInputEditText
    private lateinit var editTextBodyWeight: TextInputEditText
    private lateinit var switchHasPain: Switch
    private lateinit var editTextPainDescription: TextInputEditText
    private lateinit var editTextNotes: TextInputEditText
    private lateinit var buttonSaveWorkout: MaterialButton

    private var workoutId: Int = -1
    private var formAlreadyFilled: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutId = arguments?.getInt("workoutId") ?: -1

        getViewReferences(view)
        configureToolbar()
        configureSaveButton()
        configureScreenMode()
    }

    private fun getViewReferences(view: View) {
        topAppBar = view.findViewById(R.id.topAppBar)
        editTextDate = view.findViewById(R.id.editTextDate)
        editTextTraining = view.findViewById(R.id.editTextTraining)
        editTextBodyWeight = view.findViewById(R.id.editTextBodyWeight)
        switchHasPain = view.findViewById(R.id.switchHasPain)
        editTextPainDescription = view.findViewById(R.id.editTextPainDescription)
        editTextNotes = view.findViewById(R.id.editTextNotes)
        buttonSaveWorkout = view.findViewById(R.id.buttonSaveWorkout)
    }

    private fun configureToolbar() {
        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_save -> {
                    saveWorkout()
                    true
                }

                else -> false
            }
        }
    }

    private fun configureSaveButton() {
        buttonSaveWorkout.setOnClickListener {
            saveWorkout()
        }
    }

    private fun configureScreenMode() {
        if (workoutId == -1) {
            configureAddMode()
        } else {
            configureEditMode()
        }
    }

    private fun configureAddMode() {
        topAppBar.title = getString(R.string.toolbarAddWorkout)
        buttonSaveWorkout.text = getString(R.string.save_workout)

        editTextDate.setText("")
        editTextTraining.setText("")
        editTextBodyWeight.setText("")
        switchHasPain.isChecked = false
        editTextPainDescription.setText("")
        editTextNotes.setText("")
    }

    private fun configureEditMode() {
        topAppBar.title = "Edit workout"
        buttonSaveWorkout.text = "Update workout"

        workoutViewModel.getWorkoutById(workoutId).observe(viewLifecycleOwner) { workout ->
            if (workout != null && !formAlreadyFilled) {
                fillFormWithWorkout(workout)
                formAlreadyFilled = true
            }
        }
    }

    private fun fillFormWithWorkout(workout: Workout) {
        editTextDate.setText(workout.date)
        editTextTraining.setText(workout.training)

        if (workout.bodyWeight != null) {
            editTextBodyWeight.setText(workout.bodyWeight.toString())
        } else {
            editTextBodyWeight.setText("")
        }

        switchHasPain.isChecked = workout.hasPain
        editTextPainDescription.setText(workout.painDescription ?: "")
        editTextNotes.setText(workout.notes ?: "")
    }

    private fun saveWorkout() {
        val date = editTextDate.text.toString().trim()
        val training = editTextTraining.text.toString().trim()
        val bodyWeightText = editTextBodyWeight.text.toString().trim()
        val hasPain = switchHasPain.isChecked
        val painDescription = editTextPainDescription.text.toString().trim()
        val notes = editTextNotes.text.toString().trim()

        if (training.isEmpty()) {
            editTextTraining.error = "Training cannot be empty"
            return
        }

        if (date.isEmpty()) {
            editTextDate.error = "Date cannot be empty"
            return
        }

        val bodyWeight: Double? = if (bodyWeightText.isEmpty()) {
            null
        } else {
            bodyWeightText.toDoubleOrNull()
        }

        if (bodyWeightText.isNotEmpty() && bodyWeight == null) {
            editTextBodyWeight.error = "Enter a valid number"
            return
        }

        val workout = Workout(
            id = if (workoutId == -1) 0 else workoutId,
            date = date,
            training = training,
            bodyWeight = bodyWeight,
            hasPain = hasPain,
            painDescription = if (painDescription.isEmpty()) null else painDescription,
            notes = if (notes.isEmpty()) null else notes
        )

        if (workoutId == -1) {
            workoutViewModel.insertWorkout(workout)
            Toast.makeText(requireContext(), "Workout saved", Toast.LENGTH_SHORT).show()
        } else {
            workoutViewModel.updateWorkout(workout)
            Toast.makeText(requireContext(), "Workout updated", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }
}