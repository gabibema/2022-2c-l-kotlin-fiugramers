package com.example.aulasapp.classes

import android.content.Context
import android.content.Intent
import com.example.aulasapp.RegistroActivity

class Registro {

    fun ingresar(view: Context){
        val registroIntent = Intent(view, RegistroActivity::class.java)
        view.startActivity(registroIntent)
    }
}