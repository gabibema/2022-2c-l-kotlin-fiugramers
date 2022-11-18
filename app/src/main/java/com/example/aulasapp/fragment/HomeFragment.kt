package com.example.aulasapp.fragment

import android.annotation.SuppressLint
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
import com.example.aulasapp.classes.AulaFacultad
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var aulas: ArrayList<Aula>
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CostumAdapter
    private lateinit var email:String
    private lateinit var persona: Persona
    private var rol:Number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        email = arguments?.get("email").toString()
        db = FirebaseFirestore.getInstance()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewHome)

        recyclerView.layoutManager = LinearLayoutManager(context)

        aulas = arrayListOf()

        val card = R.layout.card_layout_home

        db.collection("usuarios").document(email).get().addOnSuccessListener {
            rol = it.data?.get("rol") as Number

            crearPersona()
            verificarTitulo()

            adapter =
                CostumAdapter(aulas, rol, onClickDelete = { id -> reservarAula(id) }, card, "Home")
            recyclerView.adapter = adapter

            generarAulas()
        }
    }

    private fun crearPersona() {
        persona = if(rol == 1){
            Profesor(email, "", "")
        }else{
            Alumno(email,"","")
        }
    }

    private fun verificarTitulo() {
        val titulo = view?.findViewById<TextView>(R.id.home_title)
        titulo!!.text = persona.obtenerTitulo()
    }

    private fun reservarAula(id: String) {
        persona.reservar(id,aulas,adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun generarAulas() {
        val facultad = AulaFacultad()
        aulas = facultad.generar(db,adapter)
    }
}