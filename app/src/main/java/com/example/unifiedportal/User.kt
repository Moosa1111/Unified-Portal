package com.example.unifiedportal

import android.widget.EditText

data class User(
    var fname : String? = null,
    var lname : String? = null,
    var inputEmail : String? = null,
    var category : String? = null,
    var amount : Double? = null,
    var note : String? = null,
    var uid : String? = null,
)