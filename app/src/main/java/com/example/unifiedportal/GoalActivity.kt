package com.example.unifiedportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AddGoalActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var userDocumentRef: DocumentReference

    private lateinit var goalEditText: EditText
    private lateinit var completedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        // Initialize Firebase Firestore and get the reference to the user's document
        firestore = FirebaseFirestore.getInstance()
        userDocumentRef = firestore.collection("users").document("user-id")

        // Get references to the UI elements
        goalEditText = findViewById(R.id.goalEditText)
        completedCheckBox = findViewById(R.id.completedCheckBox)

        // Set a click listener for the "Save" button
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveGoal()
        }
    }

    private fun saveGoal() {
        // Get the goal text and completion status
        val goal = goalEditText.text.toString()
        val completed = completedCheckBox.isChecked

        // Create a new goal document with the data
        val goalData = hashMapOf(
            "goal" to goal,
            "completed" to completed
        )

        // Add the goal document to the user's sub-collection
        userDocumentRef.collection("goals")
            .add(goalData)
            .addOnSuccessListener {
                // Goal was saved successfully
                Toast.makeText(this, "Goal saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                // There was an error saving the goal
                Toast.makeText(this, "Error saving goal.", Toast.LENGTH_SHORT).show()
            }
    }
}
