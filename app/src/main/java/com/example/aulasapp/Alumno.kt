package com.example.aulasapp

import com.example.aulasapp.adapter.CostumAdapter

class Alumno(
    override var email: String,
    override var apellido: String,
    override var nombre: String
) :Persona {
    override fun constructor(nombre: String, apellido: String, email: String) {
        this.nombre = nombre
        this.apellido = apellido
        this.email = email
    }

    override fun reservar(id: String, aulas: ArrayList<Aula>, adapter: CostumAdapter) {
        TODO("Not yet implemented")
    }

}