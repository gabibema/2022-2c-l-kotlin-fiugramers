package com.example.aulasapp.persona

import android.content.Context
import com.example.aulasapp.Reporte.Companion.listaActividades
import com.example.aulasapp.adapter.CostumAdapter
import com.example.aulasapp.aula.Aula
import com.example.aulasapp.classes.Error
import com.example.aulasapp.fragment.HomeFragment
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

    fun reservar(
        id: String,
        aulas: ArrayList<Aula>,
        adapter: CostumAdapter,
        view: Context?
    ) {

        val aula = db.collection("aulas").document(id)
        val usuario = db.collection("usuarios").document(email)

        usuario.get().addOnSuccessListener { snapshoot ->
            var contador = snapshoot.data?.get("cantidadAulas") as Number
            if (contador.toInt() < 3){
                contador = contador.toInt() + 1
                usuario.update("cantidadAulas",contador)
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
            }else{
                val error = Error()
                if (view != null) {

                    listaActividades.add("Intentó reservar el aula $id por $email. Ya tiene 3 reservas")
                    error.mostrar("Ya reservó 3 aulas. No puede reservar más",view)
                }
            }
        }

    }

    fun cancelar(id:String, aulas: ArrayList<Aula>, adapter: CostumAdapter){

        val aula = db.collection("aulas").document(id)
        val usuario = db.collection("usuarios").document(email)

        usuario.get().addOnSuccessListener { snapshoot ->
            var contador = snapshoot.data?.get("cantidadAulas") as Number
            contador = contador.toInt() - 1
            usuario.update("cantidadAulas",contador)
            aula.update("estado", true)
            aula.update("reservadoPor", "")

            var posicion = 0

            for (aulaAux in aulas) {
                if (aulaAux.id == id) {
                    break
                }
                posicion++
            }

            aulas.removeAt(posicion)
            listaActividades.add("Se canceló la reserva del aula $id por $email")
            adapter.notifyItemRemoved(posicion)
        }
    }
}