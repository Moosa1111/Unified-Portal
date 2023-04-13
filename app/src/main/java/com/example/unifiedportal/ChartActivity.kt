package com.example.unifiedportal

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        // Get the reference to the PieChart view
        val pieChart = findViewById<PieChart>(R.id.chart)

        // Get the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Get the reference to the expenses subcollection
        val expensesRef = database.collection("users").document(userId).collection("expenses")

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
                val entries = arrayListOf<PieEntry>()
                for ((category, amount) in categories) {
                    entries.add(PieEntry(amount.toFloat(), category))
                }

                // Set up the PieDataSet
                val pieDataSet = PieDataSet(entries, "Expenses by Category")
                pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
                pieDataSet.valueTextColor = Color.WHITE
                pieDataSet.valueTextSize = 12f

                // Set up the PieData
                val pieData = PieData(pieDataSet)
                pieData.setValueFormatter(PercentFormatter(pieChart))
                pieData.setValueTextSize(14f)
                pieData.setValueTextColor(Color.WHITE)

                // Set up the PieChart
                pieChart.setDrawHoleEnabled(false)
                pieChart.description.isEnabled = false
                pieChart.legend.isEnabled = false
                pieChart.setUsePercentValues(true)
                pieChart.setEntryLabelTextSize(12f)
                pieChart.data = pieData
                pieChart.animateY(1000)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching expenses: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        // Set up the "Add Expenditure" button
        val addExpense: Button = findViewById(R.id.AddExpenditure)
        addExpense.setOnClickListener{
            val intent = Intent(this, ExpenditureActivity::class.java)
            startActivity(intent)
        }
    }
}