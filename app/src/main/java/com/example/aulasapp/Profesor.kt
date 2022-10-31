package com.example.aulasapp

class Profesor(
    override var email: String,
    override var apellido: String,
    override var nombre: String
) :Persona {
    override fun constructor(nombre: String, apellido: String, email: String) {
        this.nombre = nombre
        this.apellido = apellido
        this.email = email
    }

    override fun reservar() {
        // aca hay que verificar si el aula que tocó está disponible
    }
}