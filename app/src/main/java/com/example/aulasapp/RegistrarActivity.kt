package com.example.aulasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegistrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        var nombre = findViewById<TextView>(R.id.nombre).text.toString()
        var apellido = findViewById<TextView>(R.id.apellido).text.toString()
        var btn_guardar = findViewById<TextView>(R.id.guardar)
        ingresarRegistro(nombre, apellido)
    }

    private fun ingresarRegistro(nombre:String, apellido:String){
        val persona = Persona(nombre,apellido)

    }

}
