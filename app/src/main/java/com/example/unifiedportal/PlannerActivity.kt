package com.example.unifiedportal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_planner.*
import java.text.SimpleDateFormat
import java.util.*

class PlannerActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private val events = mutableMapOf<Date, MutableList<Event>>()
    private lateinit var addevent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        auth = FirebaseAuth.getInstance()

        calendarView.setOnDateChangeListener { _, i, i1, i2 ->
            year = i
            month = i1
            day = i2
        }

        addevent = findViewById(R.id.button)
        addevent.setOnClickListener {
            createEventOnChosenDate()
        }
    }

    private fun createEventOnChosenDate() {
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        val chosenDate = cal.time

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Add Event")


        val inputEventName = EditText(this)
        inputEventName.hint = "Event Name"
        dialog.setView(inputEventName)

        dialog.setPositiveButton("Ok") { _, _ ->
            val eventName = inputEventName.text.toString()


           // val eventDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
           // val eventDateTime = eventDateFormat.parse("$year-${month+1}-$day $eventTime") ?: Date()

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid

                val event = hashMapOf(
                    "Upcoming event" to eventName
                )

                db.collection("users")
                    .document(userId)
                    .collection("events")
                    .add(event)
                    .addOnSuccessListener {
                        Log.d(TAG, "Event name added to Firestore with ID: ${it.id}")
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Error adding event name to Firestore", it)
                    }
            } else {
                Log.w(TAG, "No current user found")
            }
        }

        dialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        dialog.show()
    }


    companion object {
        private const val TAG = "PlannerActivity"
    }
}

data class Event(val name: String, val time: Date)
