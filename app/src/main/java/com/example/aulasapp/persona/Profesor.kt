package com.example.aulasapp.persona

import com.example.aulasapp.Reporte.Companion.listaActividades
import com.example.aulasapp.adapter.CostumAdapter
import com.example.aulasapp.aula.Aula
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profesor(
    override var email: String,
    override var apellido: String,
    override var nombre: String
) : Persona {

    private val db = Firebase.firestore
    private val tituloHome = "RESERVA TU AULA"
    private val tituloReserva = "MIS RESERVAS"
    private val textRol = "Profesor"
    private val rol = 1

    override fun constructor(email: String, apellido: String, nombre: String) {
        this.email = email
        this.apellido = apellido
        this.nombre = email

    }

    override fun obtenerTitulo(espacio: String): String {
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

    fun reservar(id: String, aulas: ArrayList<Aula>, adapter: CostumAdapter) {

        val aula = db.collection("aulas").document(id)
        aula.update("estado", false)
        aula.update("reservadoPor", email)
        var posicion = 0

        for (aulaAux in aulas) {
            if (aulaAux.id == id) {
                break
            }
            posicion++
        }

        aulas.removeAt(posicion)
        listaActividades.add("Se reservó el aula $id por $email")
        adapter.notifyItemRemoved(posicion)
    }

    fun cancelar(id:String, aulas: ArrayList<Aula>, adapter: CostumAdapter){
        val aula = db.collection("aulas").document(id)
        aula.update("estado", true)
        aula.update("reservadoPor","")
        var posicion = 0

        for (aulaAux in aulas){
            if(aulaAux.id == id){
                break
            }
            posicion++
        }

        aulas.removeAt(posicion)
        adapter.notifyItemRemoved(posicion)
    }
}