package com.example.aulasapp.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aulasapp.Aula
import com.example.aulasapp.CostumAdapter
import com.example.aulasapp.R
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

class ReservasFragment : Fragment(R.layout.fragment_reserva) {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var aulas: ArrayList<Aula>
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CostumAdapter
    private lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserva, container, false)
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

        recyclerView = view.findViewById(R.id.card_view_reserva)

        recyclerView.layoutManager = LinearLayoutManager(context)

        aulas = arrayListOf()

        adapter =
            CostumAdapter(aulas, onClickDelete = { id -> cancelarReserva(id)})

        recyclerView.adapter = adapter

        generarAulas()

    }

    private fun cancelarReserva(id: String) {
        val aula = db.collection("aulas").document(id)
        aula.update("estado", true)
        aula.update("reservadoPor","")
        var posicion = 0

        for (aulaAux in aulas){
            if(aulaAux.id == id){
                break
            }
            posicion++
        }

        aulas.removeAt(posicion)
        adapter.notifyItemRemoved(posicion)
    }

    private fun generarAulas() {
        db = FirebaseFirestore.getInstance()
        db.collection("aulas")
            .get()
            .addOnSuccessListener { result ->
                for (aula in result) {

                    if (aula.data["reservadoPor"] == email && aula.data["estado"] == "Ocupado") {
                        aulas.add(Aula(aula.id, "Ocupado"))
                    }

                    adapter.notifyDataSetChanged()
                }
            }
    }
}