package com.example.aulasapp.classes

import android.content.Context
import android.content.Intent
import com.example.aulasapp.RegistroGoogleActivity

class RegistroGoogle(nombre:String,email:String) {
    private var email = email
    private var nombre = nombre

    fun ingresar(view: Context) {
        val registroGoogleIntent = Intent(view, RegistroGoogleActivity::class.java)
        registroGoogleIntent.putExtra("email",email)
        registroGoogleIntent.putExtra("nombre",nombre)
        view.startActivity(registroGoogleIntent)
    }
}