package com.example.aulasapp.aula

import com.example.aulasapp.persona.Persona
import com.google.firebase.firestore.QueryDocumentSnapshot

class AulaReserva {

    fun agregar(aula: QueryDocumentSnapshot, aulas: ArrayList<Aula>, persona: Persona) {
        if (persona.obtenerRol() == 1 && aula.data.get("reservadoPor") == persona.email && aula.data["estado"] == false)
            aulas.add(Aula(aula.id, "Ocupado"))

        if(persona.obtenerRol() == 2 && aula.data["estado"] == false)
            aulas.add(Aula(aula.id, "Ocupado"))
    }
}