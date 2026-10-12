package com.example.training_diary_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Workout::class],//entity is equal to table, so in this case we only have the table (entity) "Workout"
    version = 1,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract val workoutDao: WorkoutDao

    companion object {

        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkoutDatabase::class.java,
                        "workout_database"
                    )
                        .fallbackToDestructiveMigration(dropAllTables = true)
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}