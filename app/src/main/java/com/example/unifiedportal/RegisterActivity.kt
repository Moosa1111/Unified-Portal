package com.example.unifiedportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        email = findViewById(R.id.textEmail)
        password = findViewById(R.id.password)
        firstname = findViewById(R.id.textFirstName)
        lastname = findViewById(R.id.textLastName)
        //database = FirebaseDatabase.getInstance().getReference("Users")
        val logintext: TextView = findViewById(R.id.textSignIn)


        logintext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnregister: Button = findViewById(R.id.buttonRegister)

        btnregister.setOnClickListener{
            val sName = firstname.text.toString().trim()
            val lName = lastname.text.toString().trim()
            val sEmail = email.text.toString().trim()

            val userMap = hashMapOf(
                "First name" to sName,
                "Last name" to lName,
                "Email" to sEmail
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            database.collection("users").document(userId).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Data Added Successfully",Toast.LENGTH_SHORT)
                        .show()
                    performSignUp()

                }
        }

    }

    private fun performSignUp() {
        val email: EditText = findViewById(R.id.textEmail)
        val password: EditText = findViewById(R.id.password)
        val firstname: EditText = findViewById(R.id.textFirstName)
        val lastname: EditText = findViewById(R.id.textLastName)

        if(email.text.isEmpty() || password.text.isEmpty() || firstname.text.isEmpty() || lastname.text.isEmpty()){
            Toast.makeText(this,"Please Fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, let move to the next activity i.e HomeActivity
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid // get the user id
                    val sName = firstname.text.toString().trim()
                    val lName = lastname.text.toString().trim()
                    val sEmail = email.text.toString().trim()

                    val userMap = hashMapOf(
                        "First name" to sName,
                        "Last name" to lName,
                        "Email" to sEmail,
                        "User id" to userId // add user id to the map
                    )

                    database.collection("users").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Data Added Successfully",Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this,HomeActivity::class.java)
                            startActivity(intent)

                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed. $",
                        Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error Ocuurred ${it.localizedMessage}",Toast.LENGTH_SHORT)
                    .show()
            }
    }

}