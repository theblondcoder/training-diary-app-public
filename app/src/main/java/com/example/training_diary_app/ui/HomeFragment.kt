package com.example.training_diary_app.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.training_diary_app.R
import com.example.training_diary_app.data.Workout
import com.example.training_diary_app.viewmodel.WorkoutViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val workoutViewModel: WorkoutViewModel by viewModels()

    private lateinit var topAppBar: MaterialToolbar

    private lateinit var btnAddWorkout: MaterialButton
    private lateinit var btnWorkoutHistory: MaterialButton

    private lateinit var tvTotalWorkoutsValue: TextView
    private lateinit var tvMonthWorkoutsValue: TextView
    private lateinit var tvLastWeightValue: TextView
    private lateinit var tvRecentPainValue: TextView

    private lateinit var tvPainTag: TextView
    private lateinit var tvDatePain: TextView
    private lateinit var tvLastWorkoutSummary: TextView

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewReferences(view)
        configureToolbar()
        configureButtons()
        observeWorkouts()
    }

    private fun getViewReferences(view: View) {
        topAppBar = view.findViewById(R.id.topAppBar)

        btnAddWorkout = view.findViewById(R.id.btnAddWorkout)
        btnWorkoutHistory = view.findViewById(R.id.btnWorkoutHistory)

        tvTotalWorkoutsValue = view.findViewById(R.id.tvTotalWorkoutsValue)
        tvMonthWorkoutsValue = view.findViewById(R.id.tvMonthWorkoutsValue)
        tvLastWeightValue = view.findViewById(R.id.tvLastWeightValue)
        tvRecentPainValue = view.findViewById(R.id.tvRecentPainValue)

        tvPainTag = view.findViewById(R.id.tvPainTag)
        tvDatePain = view.findViewById(R.id.tvDatePain)
        tvLastWorkoutSummary = view.findViewById(R.id.tvLastWorkoutSummary)
    }

    private fun configureToolbar() {
        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_info -> {
                    findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun configureButtons() {
        btnAddWorkout.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addEditWorkoutFragment)
        }

        btnWorkoutHistory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_workoutListFragment)
        }
    }

    private fun observeWorkouts() {
        workoutViewModel.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            updateSummary(workouts)
            updateLastWorkout(workouts)
        }
    }

    private fun updateSummary(workouts: List<Workout>) {
        val totalWorkouts = workouts.size

        val workoutsThisMonth = workouts.count { workout ->
            isWorkoutFromCurrentMonth(workout.date)
        }

        val lastWeight = workouts
            .sortedByDescending { workout -> getDateTime(workout.date) }
            .firstOrNull { workout -> workout.bodyWeight != null }
            ?.bodyWeight

        val recentPainCount = workouts.count { workout ->
            workout.hasPain
        }

        tvTotalWorkoutsValue.text = totalWorkouts.toString()
        tvMonthWorkoutsValue.text = workoutsThisMonth.toString()
        tvLastWeightValue.text = lastWeight?.toString() ?: "-"
        tvRecentPainValue.text = recentPainCount.toString()
    }

    private fun updateLastWorkout(workouts: List<Workout>) {
        val lastWorkout = workouts
            .sortedByDescending { workout -> getDateTime(workout.date) }
            .firstOrNull()

        if (lastWorkout == null) {
            tvDatePain.text = "-"
            tvLastWorkoutSummary.text = "Add your first workout to see it here."
            tvPainTag.visibility = View.GONE
            return
        }

        tvDatePain.text = lastWorkout.date

        val weightText = if (lastWorkout.bodyWeight != null) {
            "${lastWorkout.bodyWeight} kg"
        } else {
            "No weight"
        }

        val painText = if (lastWorkout.hasPain) {
            lastWorkout.painDescription ?: "Pain reported"
        } else {
            "No pain"
        }

        tvLastWorkoutSummary.text = "${lastWorkout.training} · $weightText · $painText"

        if (lastWorkout.hasPain) {
            tvPainTag.visibility = View.VISIBLE
        } else {
            tvPainTag.visibility = View.GONE
        }
    }

    private fun isWorkoutFromCurrentMonth(date: String): Boolean {
        val workoutDate = try {
            dateFormatter.parse(date)
        } catch (e: Exception) {
            null
        } ?: return false

        val workoutCalendar = Calendar.getInstance()
        workoutCalendar.time = workoutDate

        val currentCalendar = Calendar.getInstance()

        val sameMonth = workoutCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
        val sameYear = workoutCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)

        return sameMonth && sameYear
    }

    private fun getDateTime(date: String): Long {
        return try {
            dateFormatter.parse(date)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}