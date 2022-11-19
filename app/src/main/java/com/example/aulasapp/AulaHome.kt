package com.example.aulasapp

import com.google.firebase.firestore.QueryDocumentSnapshot

class AulaHome {
    fun agregar(aula: QueryDocumentSnapshot, aulas: ArrayList<Aula>) {
        if (aula.data["estado"] == true) {
            aulas.add(Aula(aula.id, "Disponible"))
        }
    }
}