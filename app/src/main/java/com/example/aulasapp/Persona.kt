package com.example.aulasapp

import com.example.aulasapp.adapter.CostumAdapter

interface Persona{

    abstract var email: String
    abstract var apellido: String
    abstract var nombre: String

    fun constructor(email:String, apellido:String, nombre:String)
    fun obtenerTitulo(espacio:String): String
    fun obtenerRolText(): String
    fun obtenerRol(): Int
}