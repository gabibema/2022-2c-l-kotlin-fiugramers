package com.example.aulasapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aulasapp.*
import com.example.aulasapp.persona.Alumno
import com.example.aulasapp.persona.Persona
import com.example.aulasapp.persona.Profesor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import gravatarUrl

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PerfilFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: FirebaseFirestore
    private lateinit var logout: Button
    private lateinit var imagen : ImageView
    private lateinit var email: String
    private var rol: Number = 0
    private lateinit var persona: Persona

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
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("usuarios").document(email).get().addOnSuccessListener {
            rol = it.data?.get("rol") as Number
            logout = view.findViewById(R.id.logout)
            imagen = view.findViewById<ImageView>(R.id.imagenUser)
            crearGravatar()

            crearPersona(it.data!!["nombre"] as String , it.data!!.get("apellido") as String)
            asignarEmail()
            verificarRol()
            cerrarSesion()

            val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
            txtNombre.text = persona.nombre + " " + persona.apellido
        }
    }

    private fun crearGravatar() {

    }

    private fun crearPersona(nombre:String,apellido:String) {
        persona = if(rol.toInt() == 1){
            Profesor(email, apellido, nombre)
        }else{
            Alumno(email, apellido, nombre)
        }
    }

    private fun asignarEmail() {
        var txtEmail = view?.findViewById<TextView>(R.id.txtEmail)
        txtEmail!!.text = persona.email
    }

    private fun verificarRol() {
        var rolPerfil = view?.findViewById<TextView>(R.id.txtRol)
        rolPerfil!!.text = persona.obtenerRolText()
    }

    private fun cerrarSesion() {
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, MainActivity::class.java))
        }
    }
}