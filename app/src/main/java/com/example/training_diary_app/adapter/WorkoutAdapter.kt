package com.example.training_diary_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.training_diary_app.R
import com.example.training_diary_app.data.Workout

class WorkoutAdapter(
    private val clickListener: WorkoutListener
) : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvWorkoutTitle: TextView = itemView.findViewById(R.id.tvWorkoutTitle)
        private val tvWorkoutDate: TextView = itemView.findViewById(R.id.tvWorkoutDate)
        private val tvWorkoutWeight: TextView = itemView.findViewById(R.id.tvWorkoutWeight)
        private val tvWorkoutPain: TextView = itemView.findViewById(R.id.tvWorkoutPain)

        fun bind(item: Workout, clickListener: WorkoutListener) {
            tvWorkoutTitle.text = item.training
            tvWorkoutDate.text = item.date

            tvWorkoutWeight.text = if (item.bodyWeight != null) {
                "Weight: ${item.bodyWeight} kg"
            } else {
                "No weight registered"
            }

            if (item.hasPain) {
                tvWorkoutPain.visibility = View.VISIBLE
                tvWorkoutPain.text = "Pain: ${item.painDescription ?: "Not specified"}"
            } else {
                tvWorkoutPain.visibility = View.VISIBLE
                tvWorkoutPain.text = "No pain"
            }

            itemView.setOnClickListener {
                clickListener.onClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): WorkoutViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_workout, parent, false)
                return WorkoutViewHolder(view)
            }
        }
    }
}

class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {

    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }
}

class WorkoutListener(
    val clickListener: (workoutId: Int) -> Unit
) {
    fun onClick(workout: Workout) = clickListener(workout.id)
}