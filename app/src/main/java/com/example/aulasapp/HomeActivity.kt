package com.example.aulasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var btn_logout = findViewById<Button>(R.id.logout)
        ingresarHome(btn_logout);
    }

    private fun ingresarHome(logout:Button){
        logout.setOnClickListener{
            onBackPressed()
        }
    }
}