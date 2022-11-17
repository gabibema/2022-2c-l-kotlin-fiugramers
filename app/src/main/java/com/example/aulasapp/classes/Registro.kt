package com.example.aulasapp.classes

import android.content.Context
import android.content.Intent
import com.example.aulasapp.RegistrarActivity

class Registro {

    fun ingresar(view: Context){
        val registroIntent = Intent(view, RegistrarActivity::class.java)
        view.startActivity(registroIntent)
    }
}