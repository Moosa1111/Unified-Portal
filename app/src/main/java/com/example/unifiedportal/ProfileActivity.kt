package com.example.unifiedportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore
    private lateinit var displayname: EditText
    private lateinit var address: EditText
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var phone: EditText
    private lateinit var save: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = Firebase.auth
        displayname = findViewById(R.id.editTextdisplayname)
        phone = findViewById(R.id.editTextPhone)
        firstname = findViewById(R.id.firstnameedittext)
        lastname = findViewById(R.id.editTextlastname)
        address = findViewById(R.id.editTextTextPostalAddress)
        save = findViewById(R.id.buttonsave)

        save.setOnClickListener {
            val fName = firstname.text.toString().trim()
            val lName = lastname.text.toString().trim()
            val dName = displayname.text.toString().trim()
            val addr = address.text.toString().trim()
            val phon = phone.text.toString().toDouble()

            val userMap = hashMapOf(
                "First name" to fName,
                "Last name" to lName,
                "Display Name" to dName,
                "Address" to addr,
                "Phone" to phon
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            database.collection("users").document(userId).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT)
                        .show()
                }


        }
    }
}