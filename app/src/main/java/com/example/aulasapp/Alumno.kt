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

    override fun reservar(id: String, aulas: ArrayList<Aula>, adapter: CostumAdapter) {
        TODO("Not yet implemented")
    }

    override fun obtenerRol(): String {
        return textRol
    }
}