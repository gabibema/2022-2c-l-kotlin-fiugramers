package com.example.aulasapp

import com.example.aulasapp.adapter.CostumAdapter

interface Persona{

    abstract var email: String
    abstract var apellido: String
    abstract var nombre: String

    fun constructor(nombre:String, apellido:String, email:String)
    // deberia estar solo en profesor, puede sacarse de interfaz
    fun reservar(id: String, aulas: ArrayList<Aula>, adapter: CostumAdapter)
    fun obtenerTitulo(): CharSequence?

}