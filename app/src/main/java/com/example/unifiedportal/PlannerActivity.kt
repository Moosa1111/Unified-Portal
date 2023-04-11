package com.example.unifiedportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_planner.*

class PlannerActivity : AppCompatActivity() {

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var addevent: Button = findViewById(R.id.button)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        calendarView.setOnDateChangeListener {
                calendarView, i, i1, i2 ->
            year = i
            month = i1+1
            day = i2
        }

        addevent.setOnClickListener{
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)
        }


    }
}