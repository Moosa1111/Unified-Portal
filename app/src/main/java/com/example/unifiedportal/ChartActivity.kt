package com.example.unifiedportal

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
    val addExpense: Button = findViewById(R.id.AddExpenditure)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        addExpense.setOnClickListener{
            val intent = Intent(this, ExpenditureActivity::class.java)
            startActivity(intent)
        }

        // Get the expense category and amount data from the previous activity
        val categories = intent.getStringArrayListExtra("categories") ?: arrayListOf()
        val amounts = intent.getDoubleArrayExtra("amounts") ?: doubleArrayOf()

        // Get the reference to the PieChart view
        val pieChart = findViewById<PieChart>(R.id.chart)

        // Create a list of PieEntry objects representing the data to be displayed in the chart
        val entries = arrayListOf<PieEntry>()
        for (i in 0 until categories.size) {
            entries.add(PieEntry(amounts[i].toFloat(), categories[i]))
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
}
