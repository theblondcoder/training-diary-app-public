package com.example.training_diary_app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutDao {

// To get all the worksout
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): LiveData<List<Workout>>

// To get a workout by ID
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkoutById(workoutId: Int): LiveData<Workout>

//To get a workout by a specifyc DATE
    @Query("SELECT * FROM workouts WHERE date = :date ORDER BY id DESC")
    fun getWorkoutsByDate(date: String): LiveData<List<Workout>>

//To get all the workouts with pain
    @Query("SELECT * FROM workouts WHERE hasPain = 1 ORDER BY date DESC")
    fun getWorkoutsWithPain(): LiveData<List<Workout>>

    @Query("SELECT * FROM workouts ORDER BY date DESC LIMIT 5")
    fun getRecentWorkouts(): LiveData<List<Workout>>

    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)
}