package com.example.training_diary_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val training: String,
    val bodyWeight: Double?,
    val hasPain: Boolean,
    val painDescription: String?,
    val notes: String?
)
