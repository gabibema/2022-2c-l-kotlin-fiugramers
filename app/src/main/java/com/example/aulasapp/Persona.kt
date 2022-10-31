package com.example.aulasapp

interface Persona{

    abstract var email: String
    abstract var apellido: String
    abstract var nombre: String

    fun constructor(nombre:String, apellido:String, email:String)
    fun reservar()

}