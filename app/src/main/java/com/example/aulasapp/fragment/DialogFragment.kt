package com.example.aulasapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import com.example.aulasapp.HomeActivity
import com.example.aulasapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogFragment : androidx.fragment.app.DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var alumno :RadioButton
    private lateinit var profesor :RadioButton
    private lateinit var email:String
    private lateinit var nombre:String
    private lateinit var aceptarButton: Button
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        email = arguments?.get("email").toString()
        nombre = arguments?.get("nombre").toString()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alumno = view.findViewById(R.id.alumno)
        profesor = view.findViewById(R.id.profesor)
        aceptarButton = view.findViewById(R.id.aceptar)

        verificarRol()
        aceptar()

    }

    private fun verificarRol(){
        if (profesor.isChecked) {
            guardarBaseDatos(email, nombre, "", 1)
        } else if (alumno.isChecked) {
            guardarBaseDatos(email, nombre, "", 2)
        }
    }

    private fun guardarBaseDatos(email:String,nombre:String,apellido:String,rol:Int){
        println("Se graba bd")
        db.collection("usuarios")
            .document(email).set(
                hashMapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "rol" to rol
                )
            )
    }

    private fun aceptar(){
        aceptarButton.setOnClickListener{
            val homeIntent = Intent(this.context, HomeActivity::class.java)
            homeIntent.putExtra("email",email)
            startActivity(homeIntent)
        }
    }
}