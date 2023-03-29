package com.example.unifiedportal

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val profile: Button = findViewById(R.id.profile)
        val expenditure: Button = findViewById(R.id.budget)
        val planner: Button = findViewById(R.id.planner)
        val chart: Button = findViewById(R.id.chart)

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
    }
}