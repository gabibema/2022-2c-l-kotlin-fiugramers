package com.example.aulasapp

class Persona {

    var nombre:String
    var apellido:String

    init {
        nombre = ""
        apellido = ""
    }

    constructor(nombre:String,apellido:String){
        this.nombre = nombre
        this.apellido = apellido
    }

    fun obtenerNombre(): String {
        return this.nombre
    }

}