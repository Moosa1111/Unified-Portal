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
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val logintext: TextView = findViewById(R.id.textSignIn)


        logintext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnregister: Button = findViewById(R.id.buttonRegister)

        btnregister.setOnClickListener{
            performSignUp()
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

                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)

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