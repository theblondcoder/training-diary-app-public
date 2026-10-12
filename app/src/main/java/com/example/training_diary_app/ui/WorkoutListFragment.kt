package com.example.training_diary_app.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.training_diary_app.R
import com.example.training_diary_app.adapter.WorkoutAdapter
import com.example.training_diary_app.adapter.WorkoutListener
import com.example.training_diary_app.data.Workout
import com.example.training_diary_app.viewmodel.WorkoutViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {

    private val workoutViewModel: WorkoutViewModel by viewModels()

    private lateinit var workoutAdapter: WorkoutAdapter

    private lateinit var chipAll: Chip
    private lateinit var chipOnlyPain: Chip
    private lateinit var chipRecent: Chip

    private var currentWorkoutsSource: LiveData<List<Workout>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        val fabAddWorkout = view.findViewById<FloatingActionButton>(R.id.fabAddWorkout)
        val rvWorkouts = view.findViewById<RecyclerView>(R.id.rvWorkouts)

        chipAll = view.findViewById(R.id.chipAll)
        chipOnlyPain = view.findViewById(R.id.chipOnlyPain)
        chipRecent = view.findViewById(R.id.chipRecent)

        configureToolbar(topAppBar)
        configureFab(fabAddWorkout)
        configureRecyclerView(rvWorkouts)
        configureFilterChips()

        showAllWorkouts()
    }

    private fun configureToolbar(topAppBar: MaterialToolbar) {
        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

//        topAppBar.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.action_search -> {
//                    true
//                }
//
//                else -> false
//            }
//        }
    }

    private fun configureFab(fabAddWorkout: FloatingActionButton) {
        fabAddWorkout.setOnClickListener {
            findNavController().navigate(R.id.action_workoutListFragment_to_addEditWorkoutFragment)
        }
    }

    private fun configureRecyclerView(rvWorkouts: RecyclerView) {
        workoutAdapter = WorkoutAdapter(
            WorkoutListener { workoutId ->
                navigateToWorkoutDetail(workoutId)
            }
        )

        rvWorkouts.adapter = workoutAdapter
    }

    private fun configureFilterChips() {
        chipAll.setOnClickListener {
            showAllWorkouts()
        }

        chipOnlyPain.setOnClickListener {
            showWorkoutsWithPain()
        }

        chipRecent.setOnClickListener {
            showRecentWorkouts()
        }
    }

    private fun showAllWorkouts() {
        observeWorkouts(workoutViewModel.allWorkouts)
        markSelectedChip(chipAll)
    }

    private fun showWorkoutsWithPain() {
        observeWorkouts(workoutViewModel.getWorkoutsWithPain())
        markSelectedChip(chipOnlyPain)
    }


    //Shows last 5 workouts
    private fun showRecentWorkouts() {
        observeWorkouts(workoutViewModel.getRecentWorkouts())
        markSelectedChip(chipRecent)
    }

    private fun observeWorkouts(source: LiveData<List<Workout>>) {
        currentWorkoutsSource?.removeObservers(viewLifecycleOwner)

        currentWorkoutsSource = source

        source.observe(viewLifecycleOwner) { workouts ->
            workoutAdapter.submitList(workouts)
        }
    }

    private fun markSelectedChip(selectedChip: Chip) {
        chipAll.isChecked = false
        chipOnlyPain.isChecked = false
        chipRecent.isChecked = false

        selectedChip.isChecked = true
    }

    private fun navigateToWorkoutDetail(workoutId: Int) {
        val bundle = Bundle().apply {
            putInt("workoutId", workoutId)
        }

        findNavController().navigate(
            R.id.action_workoutListFragment_to_workoutDetailFragment,
            bundle
        )
    }
}