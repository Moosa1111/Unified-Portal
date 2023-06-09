package com.example.unifiedportal

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExpenditureActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore
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
            val category = categoryspinner.selectedItem.toString().trim()
            val amount = expenseamount.text.toString().toDouble()
            val note = expensenote.text.toString().trim()


            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            val expensesRef = database.collection("users").document(userId).collection("expenses")

            val expense = hashMapOf(
                "category" to category,
                "amount" to amount,
                "note" to note
            )

            // add the expense data to the subcollection
            expensesRef.add(expense)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding expense: ${e.message}", Toast.LENGTH_SHORT).show()
                }


                    // TODO: Save the expense to a database or other storage mechanism
                }
        }
    }
