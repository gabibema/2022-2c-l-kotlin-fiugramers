package com.example.aulasapp.classes

import com.example.aulasapp.Aula
import com.example.aulasapp.adapter.CostumAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class AulaFacultad {
    private lateinit var aulas :ArrayList<Aula>
    fun generar(db: FirebaseFirestore, adapter: CostumAdapter): ArrayList<Aula> {

        db.collection("aulas")
            .get()
            .addOnSuccessListener { result ->
                for (aula in result) {
                    agregarAula(aula)
                    adapter.notifyDataSetChanged()
                }
            }
        return aulas
    }
    private fun agregarAula(aula: QueryDocumentSnapshot){
        if (aula.data["estado"] == true) {
            aulas.add(Aula(aula.id, "Disponible"))
        }
    }
}