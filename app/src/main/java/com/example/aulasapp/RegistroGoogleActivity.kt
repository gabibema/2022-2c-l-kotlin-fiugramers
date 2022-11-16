package com.example.aulasapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistroGoogleActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var email:String
    private lateinit var nombre: String
    private lateinit var aceptar:Button
    private lateinit var alumno: RadioButton
    private lateinit var profesor: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_google)

        email = intent.getStringExtra("email").toString()
        nombre = intent.getStringExtra("nombre").toString()
        alumno = findViewById(R.id.alumno)
        profesor = findViewById(R.id.profesor)
        aceptar = findViewById(R.id.aceptar)

        aceptar.setOnClickListener{
            if (profesor.isChecked) {
                guardarBaseDatos(1)
                ingresarHome(email)
            } else if (alumno.isChecked) {
                guardarBaseDatos(2)
                ingresarHome(email)
            }else{
                mostrarError("Debe seleccionar un rol")
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun guardarBaseDatos(rol: Int){
        db.collection("usuarios")
            .document(email).set(
                hashMapOf(
                    "nombre" to nombre,
                    "apellido" to "apellido",
                    "rol" to rol
                )
            )
    }

    private fun ingresarHome(email: String){
        val homeIntent = Intent(this,HomeActivity::class.java)
        homeIntent.putExtra("email",email)
        startActivity(homeIntent)
    }


}