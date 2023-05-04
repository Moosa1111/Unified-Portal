package com.example.unifiedportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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
        val eventbutton : Button = findViewById(R.id.buttonviewevents)
        val goalbutton : Button = findViewById(R.id.buttonviewgoals)

        // Get the reference to the PieChart view
        val barChart = findViewById<PieChart>(R.id.chart)

        // Get the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Get the reference to the expenses subcollection
        val expensesRef = db.collection("users").document(userId).collection("expenses")

        // Query the expenses subcollection and group the expenses by category
        expensesRef
            .get()
            .addOnSuccessListener { result ->
                val categories = hashMapOf<String, Double>()
                for (document in result) {
                    val category = document.getString("category") ?: ""
                    val amount = document.getDouble("amount") ?: 0.0
                    if (category.isNotEmpty()) {
                        categories[category] = categories.getOrDefault(category, 0.0) + amount
                    }
                }

                // Create a list of PieEntry objects representing the data to be displayed in the chart
                val entries = arrayListOf<BarEntry>()
                categories.forEachIndexed { index, (category, amount) ->
                    entries.add(BarEntry(index.toFloat(), amount.toFloat(), category))
                }
                // Set the bar chart data and customize the appearance
                val dataSet = BarDataSet(entries, "Expenses")
                dataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()
                val data = BarData(dataSet)
                chart.data = data
                chart.setFitBars(true)
                chart.setDrawValueAboveBar(true)
                chart.description.isEnabled = false
                chart.legend.isEnabled = false
                chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                chart.xAxis.labelCount = entries.size
                chart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return entries[value.toInt()].data as String
                    }
                }

                // Redraw the chart
                chart.invalidate()
            }

// Add a listener to the Firestore expenses collection to retrieve the expenses data
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val expensesRef = db.collection("users").document(userId).collection("expenses")
        expensesRef.addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, "Error getting expenses: ${error.message}", Toast.LENGTH_SHORT).show()
            } else {
                val expensesData = value!!.documents
                    .map { it["category"] as String to (it["amount"] as Double) }
                    .groupBy { it.first }
                    .mapValues { it.value.sumOf { (_, amount) -> amount } }
                updateBarChart(expensesData)
            }
        }

// Update the bar chart whenever a new expense is added
// Add the following code inside the `addOnSuccessListener` block in your `ExpenditureActivity` class
        val homeActivityIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeActivityIntent)
        finish()

        profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        expenditure.setOnClickListener {
            val intent = Intent(this, ExpenditureActivity::class.java)
            startActivity(intent)
        }

        planner.setOnClickListener {
            val intent = Intent(this, PlannerActivity::class.java)
            startActivity(intent)
        }

        chart.setOnClickListener {
            val intent = Intent(this, ChartActivity::class.java)
            startActivity(intent)
        }
        goals.setOnClickListener {
            val intent = Intent(this, AddGoalActivity::class.java)
            startActivity(intent)
        }

        goalbutton.setOnClickListener {
            val intent = Intent(this, AllGoalsActivity::class.java)
            startActivity(intent)
        }

        eventbutton.setOnClickListener {
            val intent = Intent(this, AllEventsActivity::class.java)
            startActivity(intent)
        }



//        // Set the bar chart data and customize the appearance
//        val dataSet = BarDataSet(entries, "Expenses")
//        dataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()
//        val data = BarData(dataSet)
//        chart.data = data
//        chart.setFitBars(true)
//        chart.setDrawValueAboveBar(true)
//        chart.description.isEnabled = false
//        chart.legend.isEnabled = false
//        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        chart.xAxis.labelCount = entries.size
//        chart.xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return entries[value.toInt()].data as String
//            }
//        }
//
//        // Redraw the chart
//        chart.invalidate()
//    }

}
