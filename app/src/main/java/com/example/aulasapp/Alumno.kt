package com.example.aulasapp

import com.example.aulasapp.adapter.CostumAdapter

class Alumno(
    override var email: String,
    override var apellido: String,
    override var nombre: String
) :Persona {
    private val tituloHome = "AULAS DISPONIBLES"
    private val tituloReserva = "AULAS OCUPADAS"
    private val textRol = "Alumno"
    private val rol = 2

    override fun constructor(email: String, apellido: String, nombre: String) {
        this.email = email
        this.apellido = apellido
        this.nombre = nombre
    }

    override fun obtenerTitulo(espacio:String): String {
        return if(espacio == "Home")
            tituloHome
        else
            tituloReserva
    }


    override fun obtenerRolText(): String {
        return textRol
    }

    override fun obtenerRol(): Int {
        return this.rol
    }
}