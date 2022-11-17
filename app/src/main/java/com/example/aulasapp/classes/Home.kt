package com.example.aulasapp.classes

import android.content.Context
import android.content.Intent
import com.example.aulasapp.HomeActivity

class Home(var email:String) {

    public fun ingresarHome(view:Context){
        val homeIntent = Intent(view, HomeActivity::class.java)
        homeIntent.putExtra("email",email)
        view.startActivity(homeIntent)
    }

}