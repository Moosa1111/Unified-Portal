package com.example.unifiedportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import org.w3c.dom.Text

class ExpenditureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenditure)

        val categoryspinner: Spinner = findViewById(R.id.category_spinner)
        val addbutton: Button = findViewById(R.id.add_button)
        val expenseamount: TextView = findViewById(R.id.expense_amount)
        val expensenote: TextView = findViewById(R.id.expense_note)
        // Populate the expense category spinner with the categories defined in strings.xml
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.expense_categories,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryspinner.adapter = adapter

        // Add an onClickListener to the add_button
        addbutton.setOnClickListener {
            // Get the values entered by the user
            val category = categoryspinner.selectedItem.toString()
            val amount = expenseamount.text.toString().toDouble()
            val note = expensenote.text.toString()

            // TODO: Save the expense to a database or other storage mechanism
        }
    }
}
