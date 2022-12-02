package com.example.aulasapp.aula

import com.example.aulasapp.persona.Persona
import com.example.aulasapp.adapter.CostumAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

open class AulaApp {

    fun generar(
        adapter: CostumAdapter,
        db: FirebaseFirestore,
        pantalla: String,
        aulas: ArrayList<Aula>,
        persona: Persona
    ) {
        db.collection("aulas")
            .get()
            .addOnSuccessListener { result ->
                for (aula in result) {
                    agregar(aula,pantalla,aulas,persona)
                }

                adapter.notifyDataSetChanged()
            }
    }
    fun agregar(
        aula: QueryDocumentSnapshot,
        pantalla: String,
        aulas: ArrayList<Aula>,
        persona: Persona
    ){
        if (pantalla != "Home") AulaReserva().agregar(aula,aulas,persona) else AulaHome().agregar(aula,aulas)
    }
}