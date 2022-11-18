package com.example.aulasapp

import com.example.aulasapp.adapter.CostumAdapter
import com.google.firebase.firestore.FirebaseFirestore

class Profesor(
    override var email: String,
    override var apellido: String,
    override var nombre: String
) :Persona {

    private lateinit var db: FirebaseFirestore

    override fun constructor(nombre: String, apellido: String, email: String) {
        TODO("Not yet implemented")
    }

    override fun reservar(id: String, aulas: ArrayList<Aula>, adapter: CostumAdapter) {
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
        adapter.notifyItemRemoved(posicion)

    }

    override fun obtenerTitulo(): String {
        return "RESERVA TU AULA"
    }
}