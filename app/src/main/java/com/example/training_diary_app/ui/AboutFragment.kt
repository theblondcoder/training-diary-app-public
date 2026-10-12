package com.example.training_diary_app.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.training_diary_app.R
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.findNavController

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGoBack=view.findViewById<Button>(R.id.buttonBackAbout)
        val topAppBar = view.findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        btnGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_workoutAboutFragment_to_HomeFragment)
        }
    }
}