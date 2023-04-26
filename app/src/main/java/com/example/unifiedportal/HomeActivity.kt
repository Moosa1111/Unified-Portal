package com.example.unifiedportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    //private lateinit var upcomingEventTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val profile: Button = findViewById(R.id.profile)
        val expenditure: Button = findViewById(R.id.budget)
        val planner: Button = findViewById(R.id.planner)
        val chart: Button = findViewById(R.id.chart)
        val goals: Button = findViewById(R.id.GoalsButton)
        val upcomingEventstext: TextView = findViewById(R.id.upcomingEventTextView)

//        upcomingEventTextView = findViewById(R.id.upcomingEventTextView)
//
        // Retrieve upcoming event from Firestore
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            db.collection("users")
                .document(userId)
                .collection("events")
                .get()
                .addOnSuccessListener { result ->
                    val upcomingEvents = mutableListOf<String>()

                    for (document in result) {
                        val event = document.toObject(Event::class.java)
                        upcomingEvents.add(event.name)
                    }

                    if (upcomingEvents.isNotEmpty()) {
                        val upcomingEvents = upcomingEvents.first()
                        upcomingEventstext.text = "Upcoming event: $upcomingEvents"
                    } else {
                        upcomingEventstext.text = "No upcoming events"
                    }
                }
                .addOnFailureListener { exception ->
                    upcomingEventstext.text = "Error retrieving upcoming events"
                }
        } else {
            upcomingEventstext.text = "Please sign in to see upcoming events"
        }

        profile.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        expenditure.setOnClickListener{
            val intent = Intent(this, ExpenditureActivity::class.java)
            startActivity(intent)
        }

        planner.setOnClickListener{
            val intent = Intent(this, PlannerActivity::class.java)
            startActivity(intent)
        }

        chart.setOnClickListener{
            val intent = Intent(this, ChartActivity::class.java)
            startActivity(intent)
        }
        goals.setOnClickListener{
            val intent = Intent(this, AddGoalActivity::class.java)
            startActivity(intent)
        }
    }
}
