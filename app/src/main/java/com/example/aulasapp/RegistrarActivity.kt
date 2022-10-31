package com.example.aulasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrarActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)


        val bundle = intent.extras
        val email = bundle?.getString("email")
        var btn_guardar = findViewById<Button>(R.id.guardar)
        ingresarRegistro( email?:"",btn_guardar)
    }

    private fun ingresarRegistro( email:String,guardar:Button){

            //val profesor = Profesor(nombre,apellido,email)


                guardar.setOnClickListener {

                    var nombre = findViewById<TextView>(R.id.nombre).text.toString()
                    var apellido = findViewById<TextView>(R.id.apellido).text.toString()
                    var alumno = findViewById<RadioButton>(R.id.alumno)
                    var profesor = findViewById<RadioButton>(R.id.profesor)
                    
                    if (profesor.isActivated) {
                        guardarBaseDatos(email,nombre,apellido,1)
                    }
                    if(alumno.isActivated){
                        guardarBaseDatos(email,nombre,apellido,0)
                    }

                    ingresarHome()
                }




    }

    private fun ingresarHome() {
        val homeIntent = Intent(this,HomeActivity::class.java)
        startActivity(homeIntent)
    }

    private fun guardarBaseDatos(email:String,nombre:String,apellido:String,rol:Int){
        db.collection("usuarios")
            .document(email).set(
                hashMapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "rol" to rol
                )
            )
    }
}
