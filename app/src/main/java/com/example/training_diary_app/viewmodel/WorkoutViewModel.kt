package com.example.training_diary_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.training_diary_app.data.Workout
import com.example.training_diary_app.data.WorkoutDao
import com.example.training_diary_app.data.WorkoutDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutDao: WorkoutDao

    val allWorkouts: LiveData<List<Workout>>

    init {
        val database = WorkoutDatabase.getInstance(application)
        workoutDao = database.workoutDao
        allWorkouts = workoutDao.getAllWorkouts()
    }

    fun getWorkoutById(workoutId: Int): LiveData<Workout> {
        return workoutDao.getWorkoutById(workoutId)
    }

    fun getWorkoutsByDate(date: String): LiveData<List<Workout>> {
        return workoutDao.getWorkoutsByDate(date)
    }

    fun getWorkoutsWithPain(): LiveData<List<Workout>> {
        return workoutDao.getWorkoutsWithPain()
    }

    fun getRecentWorkouts(): LiveData<List<Workout>> {
        return workoutDao.getRecentWorkouts()
    }

    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            insertWorkoutInDatabase(workout)
        }
    }

    private suspend fun insertWorkoutInDatabase(workout: Workout) {
        withContext(Dispatchers.IO) {
            workoutDao.insertWorkout(workout)
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            updateWorkoutInDatabase(workout)
        }
    }

    private suspend fun updateWorkoutInDatabase(workout: Workout) {
        withContext(Dispatchers.IO) {
            workoutDao.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            deleteWorkoutFromDatabase(workout)
        }
    }

    private suspend fun deleteWorkoutFromDatabase(workout: Workout) {
        withContext(Dispatchers.IO) {
            workoutDao.deleteWorkout(workout)
        }
    }
}