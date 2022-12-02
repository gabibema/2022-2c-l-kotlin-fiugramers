package com.example.aulasapp.persona

import android.content.Context
import com.example.aulasapp.adapter.CostumAdapter
import com.example.aulasapp.agregarReporte
import com.example.aulasapp.aula.Aula
import com.example.aulasapp.classes.Error
import com.google.firebase.firestore.DocumentReference
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
                actualizarBaseDatos(usuario,aula,contador,false)

                val posicion = obtnerPosicion(aulas,id)

                actualizarPantalla(aulas,posicion,adapter,"Se reservó el aula $id por $email")

            }else{
                val error = Error()
                if (view != null) {

                    agregarReporte("Intentó reservar el aula $id por $email. Ya tiene 3 reservas")
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

            actualizarBaseDatos(usuario,aula,contador,true)

            val posicion = obtnerPosicion(aulas,id)

            actualizarPantalla(aulas,posicion,adapter,"Se canceló la reserva del aula $id por $email")
        }
    }

    private fun obtnerPosicion(aulas: ArrayList<Aula>, id: String): Int {
        var posicion = 0

        for (aulaAux in aulas) {
            if (aulaAux.id == id) {
                break
            }
            posicion++
        }
        return posicion
    }
    private fun actualizarPantalla(
        aulas: ArrayList<Aula>,
        posicion: Int,
        adapter: CostumAdapter,
        mensaje: String
    ) {
        aulas.removeAt(posicion)
        agregarReporte(mensaje)
        adapter.notifyItemRemoved(posicion)
    }

    private fun actualizarBaseDatos(
        usuario: DocumentReference,
        aula: DocumentReference,
        contador: Int,
        estado: Boolean
    ) {
        usuario.update("cantidadAulas",contador)
        aula.update("estado", estado)
        aula.update("reservadoPor", email)
    }
}