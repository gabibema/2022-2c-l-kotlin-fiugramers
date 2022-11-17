package com.example.aulasapp

import android.content.Context
import android.content.Intent

class Home(var email:String) {

    public fun ingresarHome(view:Context){
        val homeIntent = Intent(view,HomeActivity::class.java)
        homeIntent.putExtra("email",email)
        view.startActivity(homeIntent)
    }

}