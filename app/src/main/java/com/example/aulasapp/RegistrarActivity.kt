package com.example.aulasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrarActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        var btn_guardar = findViewById<Button>(R.id.guardar)
        ingresarRegistro(btn_guardar)
    }

    private fun ingresarRegistro(guardar:Button){

        guardar.setOnClickListener {

            var email = findViewById<TextView>(R.id.email2).text.toString()
            var password = findViewById<TextView>(R.id.password2).text.toString()
            var nombre = findViewById<TextView>(R.id.nombre).text.toString()
            var apellido = findViewById<TextView>(R.id.apellido).text.toString()
            var alumno = findViewById<RadioButton>(R.id.alumno)
            var profesor = findViewById<RadioButton>(R.id.profesor)

            if(email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
                    password)
            }

            if (nombre.isNotEmpty() && apellido.isNotEmpty()){

                if (profesor.isChecked) {
                    guardarBaseDatos(email, nombre, apellido, 1)
                }
                if (alumno.isChecked) {
                    guardarBaseDatos(email, nombre, apellido, 2)
                }
                ingresarHome()
            }else{
                mostrarError()
            }
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

    private fun mostrarError(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Debe completar todos los datos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun ingresarHome(){
        val homeIntent = Intent(this,HomeActivity::class.java)
        startActivity(homeIntent)
    }
}
