package com.example.aulasapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aulasapp.R
import com.example.aulasapp.adapter.CostumAdapter
import com.example.aulasapp.aula.Aula
import com.example.aulasapp.persona.Alumno
import com.example.aulasapp.persona.Persona
import com.example.aulasapp.persona.Profesor
import com.google.firebase.firestore.*

import com.example.aulasapp.aula.AulaApp

class ReservasFragment : Fragment(R.layout.fragment_reserva) {
    private lateinit var aulas: ArrayList<Aula>
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CostumAdapter
    private lateinit var email:String
    private lateinit var aulaApp: AulaApp
    private lateinit var persona: Persona
    private lateinit var profesor: Profesor
    private lateinit var alumno: Alumno
    private var rol:Number = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserva, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = arguments?.get("email").toString()
        recyclerView = view.findViewById(R.id.recyclerViewReserva)

        recyclerView.layoutManager = LinearLayoutManager(context)

        aulas = arrayListOf()
        db.collection("usuarios").document(email).get().addOnSuccessListener {
            //if (it.exists()) {
                rol = it.data?.get("rol") as Number
                crearPersona(it.data!!["nombre"] as String, it.data!!.get("apellido") as String)
                verificarTitulo()
                adapter =
                    CostumAdapter(
                        aulas, rol, onClickDelete = { id -> cancelarReserva(id) },
                        R.layout.card_layout_reserva
                    )
                recyclerView.adapter = adapter

            generarAulas()

            //}
        }
    }

    private fun cancelarReserva(id: String) {
        profesor.cancelar(id,aulas,this.context)
        println("cancelaste aula !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }

    private fun crearPersona(nombre: String, apellido: String) {
        if(rol.toInt() == 1){
            persona = Profesor(email, apellido, nombre)
            profesor = persona as Profesor
        }else{
            persona = Alumno(email, apellido, nombre)
            alumno = persona as Alumno
        }
    }

    private fun generarAulas() {
        aulaApp = AulaApp()
        aulaApp.generar(adapter,db,"Reserva",aulas,persona)
    }

    private fun verificarTitulo(){
        val titulo = view?.findViewById<TextView>(R.id.reservar_title)
        titulo!!.text = persona.obtenerTitulo("Reserva")
    }
}