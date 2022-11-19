package com.example.aulasapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aulasapp.*
import com.example.aulasapp.R
import com.example.aulasapp.adapter.CostumAdapter
import com.example.aulasapp.aula.Aula
import com.example.aulasapp.persona.Alumno
import com.example.aulasapp.persona.Persona
import com.example.aulasapp.persona.Profesor
import com.google.firebase.firestore.*
import com.example.aulasapp.AulaApp as AulaApp


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReservasFragment : Fragment(R.layout.fragment_reserva) {
    private var param1: String? = null
    private var param2: String? = null

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
        arguments?.let { it ->
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserva, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = arguments?.get("email").toString()
        recyclerView = view.findViewById(R.id.recyclerViewReserva)

        recyclerView.layoutManager = LinearLayoutManager(context)

        aulas = arrayListOf()
        db.collection("usuarios").document(email).get().addOnSuccessListener {
            rol = it.data?.get("rol") as Number
            crearPersona()
            verificarTitulo()
            adapter =
                CostumAdapter(aulas,rol, onClickDelete = { id -> cancelarReserva(id)},R.layout.card_layout_reserva,"Mis reservas")
            recyclerView.adapter = adapter
            generarAulas()

        }
    }

    private fun cancelarReserva(id: String) {
        profesor.cancelar(id,aulas,adapter)
    }

    private fun crearPersona() {
        if(rol.toInt() == 1){
            persona = Profesor(email, "", "")
            profesor = persona as Profesor
        }else{
            persona = Alumno(email,"","")
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